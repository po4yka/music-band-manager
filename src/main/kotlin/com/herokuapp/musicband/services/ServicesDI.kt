package com.herokuapp.musicband.services

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun DI.MainBuilder.bindServices(){
    bind<GroupService>() with singleton { GroupService() }
    bind<PerformerService>() with singleton { PerformerService() }
}
