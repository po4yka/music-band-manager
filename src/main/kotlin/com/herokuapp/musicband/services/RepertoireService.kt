package com.herokuapp.musicband.services

import com.herokuapp.musicband.data.Repertoire
import com.herokuapp.musicband.data.RepertoireEntity
import org.jetbrains.exposed.sql.transactions.transaction

class RepertoireService {
    fun getAllRepertoires(): Iterable<Repertoire> = transaction {
        RepertoireEntity.all().map(RepertoireEntity::toRepertoire)
    }
}
