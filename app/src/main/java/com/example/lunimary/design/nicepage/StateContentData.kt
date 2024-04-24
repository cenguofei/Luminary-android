package com.example.lunimary.design.nicepage

import androidx.compose.ui.graphics.Color

/**
 * @property isError �Ƿ�չʾ����廭
 * @property isEmpty true->û������չʾʱ��ʾ�հײ廭
 * @property isSearchEmpty ���������������
 * @property showShimmer �Ƿ�չʾshimmer
 * @property openLoadingWheelDialog �Ƿ�չʾ�����е���
 * @property color content ����ɫ
 * @property shouldCheckLoginState �Ƿ����¼״̬�����û��¼����ת��¼ҳ��
 * @property networkState �����Ϊ[NetworkState.Default]����չʾ�������廭
 */
data class StateContentData(
    val isError: Boolean = false,
    val errorMsg: String? = null,
    val onRefreshClick: () -> Unit = {},
    val onRetryClick: () -> Unit = {},
    val isEmpty: Boolean = false,
    val emptyMsg: String? = null,

    val isSearchEmpty: Boolean = false,

    val showShimmer: Boolean = false,

    val openLoadingWheelDialog: Boolean = false,

    val color: Color = Color.Transparent,

    val shouldCheckLoginState: Boolean = false,

    val networkState: NetworkState = NetworkState.Default,
)