package com.peterstev.towerofhanoi.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultySelector(
    onSelect: (Int) -> Unit
) {
    val disks = listOf(3, 4, 5, 6, 7)
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableIntStateOf(disks[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier.clip(shape = RoundedCornerShape(18.dp)),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selected.toString(),
            onValueChange = { selected },
            readOnly = true,
            label = { Text("Number Of Disks") },
            trailingIcon = { TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            disks.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        expanded = false
                        selected = option
                        onSelect(option)
                    }
                )
            }
        }
    }
}