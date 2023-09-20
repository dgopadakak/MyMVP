package com.example.mymvp

import android.content.Context
import com.example.mymvp.model.Model
import com.example.mymvp.model.MqttHelper
import com.example.mymvp.presenter.Presenter
import com.example.mymvp.view.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [AppModule::class])
interface AppComponent
{
    fun inject(activity: MainActivity)

    @Component.Factory
    interface AppComponentFactory
    {
        fun create(@BindsInstance context: Context): AppComponent
    }
}

@Module
class AppModule
{
    @Provides
    fun providePresenter(model: Model): Presenter
    {
        return Presenter(model)
    }

    @Provides
    fun provideModel(mqttHelper: MqttHelper): Model
    {
        return Model(mqttHelper)
    }

    @Provides
    fun provideMqttHelper(context: Context): MqttHelper
    {
        return MqttHelper(context)
    }
}
