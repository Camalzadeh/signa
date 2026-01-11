package org.signa.app.ui.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import org.signa.app.ui.theme.GraphColors


@Composable
fun FilterChipItem(selected: Boolean, label: String, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = GraphColors.CyberNeon,
            selectedLabelColor = GraphColors.VoidBlack,
            labelColor = GraphColors.StarlightWhite,
            containerColor = GraphColors.DeepSpaceBlack.copy(alpha = 0.5f)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = GraphColors.CyberNeon.copy(alpha = 0.5f),
            selectedBorderColor = GraphColors.CyberNeon
        )
    )
}