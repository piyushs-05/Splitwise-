package com.example.splitwise_final.presentation.scanreceipt

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import java.io.ByteArrayOutputStream
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanReceiptScreen(
    groupId: String,
    members: List<com.example.splitwise_final.domain.model.User>,
    onBackClick: () -> Unit,
    onReceiptScanned: (String) -> Unit,
    viewModel: ScanReceiptViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var paidBy by remember { mutableStateOf(members.firstOrNull()?.id ?: "") }
    var selectedUsers by remember { mutableStateOf(members.firstOrNull()?.id?.let { setOf(it) } ?: emptySet()) }

    var showSuccess by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Check camera permission
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            selectedBitmap = it
            selectedImageUri = null
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                selectedBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Show success dialog only if expense was created
    if (state.scanResult?.expense != null && !showSuccess) {
        showSuccess = true
    }

    if (showSuccess && state.scanResult?.expense != null) {
        AlertDialog(
            onDismissRequest = {
                showSuccess = false
                viewModel.clearState()
                onReceiptScanned(state.scanResult?.expense?.id ?: "")
            },
            title = {
                Text("Receipt Scanned!", fontWeight = FontWeight.Bold, color = Color(0xFF4CB5AE))
            },
            text = {
                Column {
                    state.scanResult?.let { result ->
                        Text("Amount: ₹${result.scannedAmount}")
                        Text("Vendor: ${result.vendor}")
                        Text("Category: ${result.category}")
                        Text("\nExpense created successfully!")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccess = false
                        viewModel.clearState()
                        onReceiptScanned(state.scanResult?.expense?.id ?: "")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CB5AE)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Scan Receipt",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CB5AE)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Selection Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (hasCameraPermission) {
                            cameraLauncher.launch(null)
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CB5AE)
                    )
                ) {
                    Text("📷 Camera")
                }

                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CB5AE)
                    )
                ) {
                    Text("🖼️ Gallery")
                }
            }

            // Display selected image
            selectedBitmap?.let { bitmap ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Receipt",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            if (selectedBitmap == null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE0E0E0)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No image selected\nTap Camera or Gallery",
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Paid By
            var expandedPaidBy by remember { mutableStateOf(false) }

            Text(
                text = "Who paid?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            ExposedDropdownMenuBox(
                expanded = expandedPaidBy,
                onExpandedChange = { expandedPaidBy = it }
            ) {
                OutlinedTextField(
                    value = members.find { it.id == paidBy }?.name ?: "Select user",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Paid By") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPaidBy) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CB5AE),
                        cursorColor = Color(0xFF4CB5AE)
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedPaidBy,
                    onDismissRequest = { expandedPaidBy = false }
                ) {
                    members.forEach { member ->
                        DropdownMenuItem(
                            text = { Text(member.name) },
                            onClick = {
                                paidBy = member.id
                                expandedPaidBy = false
                            }
                        )
                    }
                }
            }

            // Split Among
            Text(
                text = "Split Among",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            members.forEach { member ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedUsers.contains(member.id),
                        onCheckedChange = { checked ->
                            selectedUsers = if (checked) {
                                selectedUsers + member.id
                            } else {
                                selectedUsers - member.id
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF4CB5AE)
                        )
                    )
                    Text(
                        text = member.name,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }

            // Error message with helpful suggestions
            if (state.error.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "⚠️ Scanning Failed",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.error,
                            color = Color(0xFFC62828),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💡 Tips:\n" +
                                    "• Use a clearer, well-lit photo\n" +
                                    "• Ensure receipt text is readable\n" +
                                    "• Try taking photo from directly above\n" +
                                    "• Or add expense manually instead",
                            color = Color(0xFF666666),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onBackClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CB5AE)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add Manually Instead")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scan Button
            Button(
                onClick = {
                    selectedBitmap?.let { bitmap ->
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                        val imageBytes = stream.toByteArray()

                        viewModel.scanReceipt(
                            imageBytes = imageBytes,
                            groupId = groupId,
                            paidByUserId = paidBy,
                            splitAmongUserIds = selectedUsers.toList()
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CB5AE)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = selectedBitmap != null && !state.isLoading && selectedUsers.isNotEmpty()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Scan Receipt & Create Expense",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

