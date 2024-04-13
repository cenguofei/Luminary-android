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
 * ��ȡ���ھ����ȥ��ʱ����죬���Ͱ�����
 *
 * { ���ڣ�**����ǰ��**Сʱǰ��**��ǰ��since.niceDateToDay }
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
                    "$days ��ǰ"
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
            return "����"
        }

        if (minutes <= 60) {
            return "$minutes ����ǰ"
        }

        val hours = TimeUnit.MILLISECONDS.toHours(differenceMillis)
        return "$hours Сʱǰ"
    }

    /**
     * �����ں�ʱ��ֿ�����������ʱ�䲿�ֵĲ��죬��˽��������ڵĲ���
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
     * �ڼ�����������ʱ�����������ں�ʱ�����������
     */
    private fun getDaysSinceTimestampLessApi26(): Long {
        val currentDate = nowCalendar
        val dateFromTimestamp = pastCalendar

        // ����������Ϊ����Ŀ�ʼʱ�䣬�Ժ���ʱ�䲿�ֵĲ���
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
     * ��ǰʱ�����[pastCalendar]�Ƿ񳬹�1��
     */
    private fun isTimeDifferenceGreaterThanOneDay(): Boolean {
        val differenceMillis = nowCalendar.timeInMillis - pastCalendar.timeInMillis
        val differenceDays = differenceMillis / (24 * 60 * 60 * 1000)

        return differenceDays > 0
    }
}