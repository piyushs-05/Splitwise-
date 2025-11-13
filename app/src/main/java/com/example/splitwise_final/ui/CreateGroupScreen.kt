package com.example.splitwise_final.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    onBackClick: () -> Unit = {},
    onCreateClick: (String, List<String>) -> Unit = { _, _ -> }
) {
    var groupName by remember { mutableStateOf(TextFieldValue("")) }
    val members = remember {
        mutableStateListOf(
            Member("Rahul", false),
            Member("Priya", false),
            Member("Sarthak", false),
            Member("Meena", false),
            Member("Ananya", false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Create Group",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CB5AE))
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Group Name Field
        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Group name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Add Members Title
        Text(
            text = "Add members",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 8.dp)
        )

        // Members list (mock data)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            members.forEach { member ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { member.isSelected = !member.isSelected },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = member.isSelected,
                        onCheckedChange = { member.isSelected = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CB5AE))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = member.name,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Create Group Button
        Button(
            onClick = {
                val selectedMembers = members.filter { it.isSelected }.map { it.name }
                onCreateClick(groupName.text, selectedMembers)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CB5AE)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Create Group",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Data class for member selection
data class Member(
    val name: String,
    var isSelected: Boolean
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateGroupScreenPreview() {
    com.example.splitwise_final.ui.theme.Splitwise_finalTheme {
        CreateGroupScreen()
    }
}
