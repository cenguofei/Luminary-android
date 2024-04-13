package com.example.lunimary.base

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lunimary.util.logd
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object TimeManager {
    private val daysSinceTimestamp: DaysSinceTimestamp = DaysSinceTimestampImpl()
    fun getDaysSinceTimestamp(timestamp: Long): String {
        return daysSinceTimestamp.getDaysSinceTimestamp(timestamp)
    }
}

/**
 * 获取现在距离过去的时间差异，类型包括：
 *
 * { 现在，**分钟前，**小时前，**天前，since.niceDateToDay }
 */
interface DaysSinceTimestamp {

    fun getDaysSinceTimestamp(since: Long): String
}

private class DaysSinceTimestampImpl : DaysSinceTimestamp {
    private val nowCalendar = Calendar.getInstance()
    private val pastCalendar = Calendar.getInstance()

    override fun getDaysSinceTimestamp(since: Long): String {
        nowCalendar.timeInMillis = System.currentTimeMillis()
        pastCalendar.timeInMillis = since
        return when (isTimeDifferenceGreaterThanOneDay()) {
            true -> {
                val days = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getDaysSinceTimestampApi26(since)
                } else {
                    getDaysSinceTimestampLessApi26()
                }
                if (days > 20) {
                    since.niceDateToDay
                } else {
                    "$days 天前"
                }
            }
            else -> { getTimeDifference(since) }
        }.also {
            "days=$it".logd("getDaysSinceTimestamp")
        }
    }

    private fun getTimeDifference(timestamp: Long): String {
        val currentTime = nowCalendar.timeInMillis
        val differenceMillis = currentTime - timestamp

        val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis)
        if (minutes == 0L) {
            return "现在"
        }

        if (minutes <= 60) {
            return "$minutes 分钟前"
        }

        val hours = TimeUnit.MILLISECONDS.toHours(differenceMillis)
        return "$hours 小时前"
    }

    /**
     * 将日期和时间分开处理，忽略了时间部分的差异，因此仅计算日期的差异
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDaysSinceTimestampApi26(timestamp: Long): Long {
        val currentDate = LocalDate.now()
        val dateFromTimestamp =
            LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC).toLocalDate()

        val diffInDays = ChronoUnit.DAYS.between(dateFromTimestamp, currentDate)
        return abs(diffInDays)
    }

    /**
     * 在计算天数差异时，考虑了日期和时间的完整差异
     */
    private fun getDaysSinceTimestampLessApi26(): Long {
        val currentDate = nowCalendar
        val dateFromTimestamp = pastCalendar

        // 将日期设置为该天的开始时间，以忽略时间部分的差异
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)

        dateFromTimestamp.set(Calendar.HOUR_OF_DAY, 0)
        dateFromTimestamp.set(Calendar.MINUTE, 0)
        dateFromTimestamp.set(Calendar.SECOND, 0)
        dateFromTimestamp.set(Calendar.MILLISECOND, 0)

        val diffInMillis = currentDate.timeInMillis - dateFromTimestamp.timeInMillis

        return diffInMillis / (24 * 60 * 60 * 1000)
    }

    /**
     * 当前时间距离[pastCalendar]是否超过1天
     */
    private fun isTimeDifferenceGreaterThanOneDay(): Boolean {
        val differenceMillis = nowCalendar.timeInMillis - pastCalendar.timeInMillis
        val differenceDays = differenceMillis / (24 * 60 * 60 * 1000)

        return differenceDays > 0
    }
}