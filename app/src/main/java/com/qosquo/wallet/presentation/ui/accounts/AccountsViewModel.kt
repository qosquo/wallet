package com.qosquo.wallet.presentation.ui.accounts

import androidx.lifecycle.ViewModel
import com.qosquo.wallet.domain.Currencies
import com.qosquo.wallet.data.db.dao.AccountsDao
import com.qosquo.wallet.data.db.entity.AccountsDbEntity
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

    fun onAction(action: AccountsAction) {
        when (action) {
            AccountsAction.SaveAccount -> {
                val id = _state.value.id
                val name = _state.value.name
                val initialBalance = _state.value.initialBalance.toFloatOrNull()
                val currency = _state.value.currency
                val mustBeCounted = _state.value.mustBeCounted
                val iconId = _state.value.iconId
                val colorHex = _state.value.colorHex

                if (name.isBlank() || iconId == 0 ||
                    colorHex.isBlank() || colorHex == "#000000") {
                    return
                }

                val newAccount = AccountsDbEntity(
                    id = id,
                    balance = initialBalance?.div(100) ?: 0F,
                    currency = currency,
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
                        currency = Currencies.RUBEL.ordinal,
                        mustBeCounted = true
                    )
                }
            }

            is AccountsAction.SetInitialBalance -> {
                _state.update { it.copy(initialBalance = action.newBalance) }
            }
            is AccountsAction.SetName -> {
                _state.update { it.copy(name = action.newName) }
            }
            is AccountsAction.SetMustBeCounted -> {
                _state.update { it.copy(mustBeCounted = action.newValue) }
            }
            is AccountsAction.SetIconId -> {
                _state.update { it.copy(iconId = action.newId) }
            }
            is AccountsAction.SetColorHex -> {
                _state.update { it.copy(colorHex = action.newHex) }
            }

            is AccountsAction.SetCurrency -> {
                _state.update { it.copy(currency = action.newCurrency) }
            }

            is AccountsAction.SetAccountById -> {
                if (action.accountId != null) {
                    val account = dao.getAccountFromId(action.accountId)
                    this.initialState = AccountsState(
                        accounts = dao.getAllAccountsData(),
                        id = account.id,
                        name = account.name,
                        initialBalance = (account.balance * 100).toInt().toString(),
                        currency = account.currency,
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