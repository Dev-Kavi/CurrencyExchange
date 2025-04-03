package com.dev.carl.currencyexchange.ui.converter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.carl.currencyexchange.ui.theme.ButtonSubmit
import java.util.Locale

@Composable
fun ExchangeDisplay(
    state: ExchangeState,
    onFromCurrencyChanged: (String) -> Unit,
    onToCurrencyChanged: (String) -> Unit,
    onConvert: (Double) -> Unit,
    onDialogDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var amount by remember { mutableStateOf("100.00") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Currency Exchange".uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(16.dp))

        SellDisplay(
            selectedCurrency = state.fromCurrency,
            onCurrencySelected = onFromCurrencyChanged,
            currencies = state.ratesData?.rates?.getCurrencies() ?: emptyList(),
            amount = amount,
            onAmountChanged = { newAmount ->
                amount = newAmount
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 42.dp, end = 42.dp, bottom = 8.dp)
        )

        ReceiveDisplay(
            selectedCurrency = state.toCurrency,
            onCurrencySelected = onToCurrencyChanged,
            currencies = state.ratesData?.rates?.getCurrencies() ?: emptyList(),
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = {
                val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                onConvert(parsedAmount)
            },
            enabled = amount.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 36.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonSubmit
            )
        ) {
            Text(
                text = "SUBMIT",
                color = Color.White,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (state.conversionDetails.isNotBlank()) {
            ConversionDialog(
                onDismissRequest = { onDialogDismissed() },
                onConfirmation = { onDialogDismissed() },
                dialogText = state.conversionDetails
            )
        }

        if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ConversionDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogText: String
) {
    AlertDialog(
        modifier = Modifier
            .alpha(.9F),
        title = {
            Text(
                text = "Currency converted",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        text = {
            Text(
                text = dialogText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Done",
                    textAlign = TextAlign.Center,
                )
            }
        },

        onDismissRequest = {
            onDismissRequest()
        }
    )
}