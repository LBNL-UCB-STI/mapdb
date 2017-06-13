package org.beammapdb

class StoreWALTxTest: StoreTxTest(){

    override fun open(): StoreTx {
        return org.mapdb.StoreWAL.Companion.make()
    }

}