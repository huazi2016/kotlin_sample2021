package com.test.base.net

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

/**
 * author : huazi
 * time   : 2021/3/9
 * desc   : ViewModel基类
 */
@KoinApiExtension
abstract class BaseViewModel: ViewModel(), LifecycleObserver, KoinComponent {

    override fun onCleared() {
        super.onCleared()
        Logger.d("BaseViewModel_onCleared")
        viewModelScope.cancel()
    }
}