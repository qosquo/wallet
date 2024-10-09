package com.qosquo.wallet.viewmodel

import androidx.lifecycle.ViewModel
import com.qosquo.wallet.Event
import com.qosquo.wallet.model.database.AccountsDao
import com.qosquo.wallet.model.database.entities.AccountsDbEntity
import com.qosquo.wallet.viewmodel.states.AccountsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AccountsViewModel(
    private val dao: AccountsDao
): ViewModel() {

//    private val _accounts = MutableStateFlow(dao.getAllAccountsData())
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(),
//            emptyList()
//        )
    private val _state: MutableStateFlow<AccountsState> = MutableStateFlow(
        AccountsState(accounts = dao.getAllAccountsData())
    )
    val state = _state.asStateFlow()

    private var initialState: AccountsState = AccountsState()

//    val state: StateFlow<AccountsState> = combine(_state, _accounts) { state, accounts ->
//        state.copy(
//            accounts = accounts
//        )
//    }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            AccountsState()
//        )

    fun canExitForm(): Boolean {
        return initialState == _state.value
    }

    fun onEvent(event: Event.AccountsEvent) {
        when (event) {
            Event.AccountsEvent.SaveAccount -> {
                val id = _state.value.id
                val name = _state.value.name
                val initialBalance = _state.value.initialBalance.toFloatOrNull()
                val mustBeCounted = _state.value.mustBeCounted
                val iconId = _state.value.iconId
                val colorHex = _state.value.colorHex

                if (name.isBlank() || iconId == 0 ||
                    colorHex.isBlank() || colorHex == "#000000" ||
                    initialBalance == null) {
                    return
                }

                val newAccount = AccountsDbEntity(
                    id = id,
                    balance = initialBalance / 100,
                    accountName = name,
                    accountIconId = iconId,
                    colorHex = colorHex,
                    count = mustBeCounted
                )

                dao.upsertNewAccountData(newAccount)
                val updatedAccounts = dao.getAllAccountsData()
//                _state.update {
//                    it.copy(accounts = updatedAccounts)
//                }
//                viewModelScope.launch {
//                    dao.insertNewAccountData(newAccount)
//                }
                _state.update {
                    it.copy(
                        accounts = updatedAccounts,
                        name = "",
                        iconId = 0,
                        colorHex = "#000000",
                        initialBalance = "",
                        mustBeCounted = true
                    )
                }
            }

            is Event.AccountsEvent.SetInitialBalance -> {
                _state.update { it.copy(initialBalance = event.newBalance) }
            }
            is Event.AccountsEvent.SetName -> {
                _state.update { it.copy(name = event.newName) }
            }
            is Event.AccountsEvent.SetMustBeCounted -> {
                _state.update { it.copy(mustBeCounted = event.newValue) }
            }
            is Event.AccountsEvent.SetIconId -> {
                _state.update { it.copy(iconId = event.newId) }
            }
            is Event.AccountsEvent.SetColorHex -> {
                _state.update { it.copy(colorHex = event.newHex) }
            }

            is Event.AccountsEvent.SetAccountById -> {
                if (event.accountId != null) {
                    val account = dao.getAccountFromId(event.accountId)
                    this.initialState = AccountsState(
                        accounts = dao.getAllAccountsData(),
                        id = account.id,
                        name = account.name,
                        initialBalance = (account.balance * 100).toInt().toString(),
                        iconId = account.accountIconId,
                        colorHex = account.colorHex,
                        mustBeCounted = account.count,
                    )
                } else {
                    this.initialState = AccountsState(
                        accounts = dao.getAllAccountsData()
                    )
                }

                _state.value = this.initialState
            }
        }
    }
}