package com.qosquo.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.qosquo.wallet.ui.screens.AccountsScreen
import com.qosquo.wallet.ui.theme.WalletTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Dependencies.init(applicationContext)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val accountsViewModel = Dependencies.accountsViewModel
        setContent {
            WalletTheme {
                AccountsScreen(viewModel = accountsViewModel)
//                val state by accountsViewModel.state.collectAsStateWithLifecycle()
//                AccountsList(state = state, onEvent = accountsViewModel::onEvent)
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WalletTheme {
        Greeting("Android")
    }
}