package com.example.lunimary.base.viewmodel

import com.example.lunimary.base.BaseResponse
import kotlinx.coroutines.Job

/**
 * ������������
 */
interface ApiRequest {
    /**
     * ��ʼ����ʱ���ã�ǰһ������û����֮ǰ����������Ч
     */
    fun fly(url: String, action: () -> Unit)

    /**
     * �������ʱ����
     */
    fun land(url: String)

    fun <T> request(
        onSuccess: (data: T?, msg: String?) -> Unit = { _, _ -> },
        emptySuccess: () -> Unit = {},
        onFailed: (msg: String) -> Unit = {},
        onFinish: () -> Unit = {},
        block: suspend () -> BaseResponse<T>
    ): Job
}