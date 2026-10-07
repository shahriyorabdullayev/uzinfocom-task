package uz.shahriyor.uzinfocom_task.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.shahriyor.uzinfocom_task.presentation.home.HomeViewModel
import uz.shahriyor.uzinfocom_task.presentation.order.OrderViewModel

val presentationModule = module {

    viewModel { HomeViewModel(get()) }

    viewModel { OrderViewModel(get()) }
}
