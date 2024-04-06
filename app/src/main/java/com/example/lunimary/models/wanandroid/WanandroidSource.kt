package com.example.lunimary.models.wanandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunimary.base.ktor.httpClient
import com.example.lunimary.base.ktor.securityPost
import com.example.lunimary.base.ktor.setJsonBody
import com.example.lunimary.models.Article
import com.example.lunimary.models.VisibleMode
import com.example.lunimary.util.logd
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface WanandroidSource {

    suspend fun page(page: Int): Wanandroid

    companion object : WanandroidSource by WanandroidSourceImpl()
}

class WanandroidSourceImpl : WanandroidSource {
    override suspend fun page(page: Int): Wanandroid {
        return httpClient.get("https://www.wanandroid.com/article/list/$page/json")
            .body()
    }
}

//page=412, pageCount=763, size=20
class WanandroidViewModel : ViewModel() {
    @Volatile
    private var pageCount = Int.MAX_VALUE
    private var page = 0

    private fun getAndPost() {
        viewModelScope.launch(Dispatchers.IO) {
            while (page < pageCount) {
                val data = WanandroidSource.page(page)
                if (pageCount == Int.MAX_VALUE) {
                    pageCount = data.data.pageCount
                }
                "page=$page, pageCount=$pageCount, size=${data.data.datas.size}".logd("WanandroidViewModel")
                data.data.datas
                    .filter {
                        !it.link.startsWith("https://juejin.cn") &&
                                !it.link.startsWith("https://mp.weixin.qq.com")
                                && !it.title.contains("技术周刊")
                                && !it.title.contains("ohos")
                                && !it.title.contains("NDK与JNI基础")
                                && !it.title.contains("Json 转 TS")
                                && !it.title.contains("Creator")
                                && !it.author.contains("鸿洋")
                                && !it.author.contains("残页")
                                && !it.author.contains("JsonChao")
                    }
                    .map {
                        val tags = it.tags.map { tag -> tag.name }.toTypedArray()
                        Article(
                            userId = 1,
                            username = "陈国飞",
                            author = it.author.ifEmpty { it.shareUser },
                            title = it.title,
                            link = it.link,
                            visibleMode = VisibleMode.PUBLIC,
                            tags = tags,
                            collections = 99,
                            comments = 99,
                            likes = 99,
                            viewsNum = 99,
                            cover = listOf(
                                "res/uploads/cgf/e442ac2a-513f-4640-8b27-0bf7fb3ebd39_IMG_20240226_144451.jpg",
                                "res/uploads/cgf/264e4c02-e27e-4bdf-9235-2efe893f3aec_IMG_20240317_153135.jpg",
                            ).random(),
                            timestamp = it.publishTime
                        )
                    }.let {
                        "size=${it.size} articles inserted".logd("WanandroidViewModel")
                        httpClient.securityPost("/wanandroid/save_articles") {
                            setJsonBody(
                                WanandroidArticles(it)
                            )
                        }
                    }
                page++
            }
        }
    }

    init {
        //getAndPost()
    }
}