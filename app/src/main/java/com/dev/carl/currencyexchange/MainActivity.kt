package com.dev.carl.currencyexchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.carl.currencyexchange.ui.converter.BalanceDisplay
import com.dev.carl.currencyexchange.ui.converter.ExchangeDisplay
import com.dev.carl.currencyexchange.ui.converter.ExchangeViewModel
import com.dev.carl.currencyexchange.ui.theme.AppBarBlue
import com.dev.carl.currencyexchange.ui.theme.CurrencyExchangeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyExchangeTheme {
                val viewModel: ExchangeViewModel by viewModels()
                val state = viewModel.state
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = AppBarBlue,
                                titleContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = "Currency converter",
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        BalanceDisplay(state = state)
                        Spacer(modifier = Modifier.height(16.dp))
                        ExchangeDisplay(
                            state = state,
                            onFromCurrencyChanged = { currency ->
                                viewModel.updateFromCurrency(currency)
                            },
                            onToCurrencyChanged = { currency ->
                                viewModel.updateToCurrency(currency)
                            },
                            onConvert = { amount ->
                                viewModel.convertCurrency(amount)
                            }, onDialogDismissed = {
                                viewModel.dismissConversionDialog()
                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

