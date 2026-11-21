package com.zanabilisim.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

private data class ServiceItem(
    val title: String,
    val description: String,
    val icon: ImageVector
)

private enum class AppTab(val label: String, val icon: ImageVector) {
    HOME("Genel", Icons.Filled.Home),
    SERVICES("Hizmetler", Icons.Filled.Build),
    WEB("Web / Mağaza", Icons.Filled.Language)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = androidx.compose.ui.graphics.Color(0xFF0F4C81),
                    secondary = androidx.compose.ui.graphics.Color(0xFF1E88E5),
                    surfaceContainerLowest = androidx.compose.ui.graphics.Color(0xFFF7F9FC)
                )
            ) {
                ZanaBilisimApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZanaBilisimApp() {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    var selectedTab by remember { mutableStateOf(AppTab.HOME) }
    var webUrl by remember { mutableStateOf("https://www.zanabilisim.com") }

    val services = remember {
        listOf(
            ServiceItem(
                title = "Bilgisayar Hastanesi",
                description = "Masaüstü, laptop, AIO ve tabletlerde arıza tespiti, anakart revizyonu, BGA çip onarımı ve sıvı teması müdahalesi.",
                icon = Icons.Outlined.Computer
            ),
            ServiceItem(
                title = "Donanım & Bakım",
                description = "Termal bakım, kasa ve menteşe onarımı, ekran ve klavye değişimi ile performans iyileştirme.",
                icon = Icons.Outlined.Memory
            ),
            ServiceItem(
                title = "Veri Kurtarma",
                description = "HDD, SSD, harici disk, USB bellek ve MicroSD kartlardan profesyonel veri kurtarma.",
                icon = Icons.Outlined.Storage
            ),
            ServiceItem(
                title = "Güvenlik & Altyapı",
                description = "IP/AHD güvenlik kamera ve alarm sistemleri, iş yeri ağ/Wi-Fi kurulumu ve yazıcı/toner desteği.",
                icon = Icons.Outlined.Videocam
            ),
            ServiceItem(
                title = "Kurumsal Toplu SMS",
                description = "İşletmeler için duyuru, bilgilendirme ve kampanya süreçlerine yönelik toplu SMS altyapısı.",
                icon = Icons.Outlined.Sms
            ),
            ServiceItem(
                title = "Satış & Yedek Parça",
                description = "Sıfır bilişim ürünleri ve 2. el orijinal notebook yedek parçaları için hızlı tedarik.",
                icon = Icons.Outlined.ShoppingBag
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "ZANA BİLİŞİM",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Bilgisayar Satış ve Teknik Servis • Batman",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { dialPhone(context, "04882210801") }) {
                        Icon(Icons.Default.Call, contentDescription = "Sabit Hattı Ara")
                    }
                    IconButton(onClick = { openWhatsApp(context, "905530720801") }) {
                        Icon(Icons.Default.Chat, contentDescription = "WhatsApp")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.HOME -> HomeScreen(
                    isTablet = isTablet,
                    onOpenStore = {
                        webUrl = "https://www.zanabilisim.com.tr"
                        selectedTab = AppTab.WEB
                    },
                    onOpenSite = {
                        webUrl = "https://www.zanabilisim.com"
                        selectedTab = AppTab.WEB
                    }
                )

                AppTab.SERVICES -> ServicesScreen(
                    services = services,
                    isTablet = isTablet
                )

                AppTab.WEB -> WebPortalScreen(
                    currentUrl = webUrl,
                    onSelectUrl = { webUrl = it }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    isTablet: Boolean,
    onOpenStore: () -> Unit,
    onOpenSite: () -> Unit
) {
    val context = LocalContext.current
    val address = "Gültepe Mah. Eflatun Cad. Kevser Apt. Altı No:5-C, 72070 Merkez / Batman"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = if (isTablet) 28.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "2016'dan Beri Güvenilir Teknik Servis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Bilgisayar hastanesi, anakart/BGA onarımı, veri kurtarma, kamera/ağ sistemleri ve orijinal parça/ürün desteğiyle Batman merkezde hizmetinizdeyiz.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Button(onClick = { openWhatsApp(context, "905530720801") }) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("WhatsApp Destek")
                        }
                        OutlinedButton(onClick = { openMap(context, address) }) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Yol Tarifi")
                        }
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "İletişim ve Adres Bilgileri",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    InfoRow(Icons.Outlined.LocationOn, "Adres: $address")
                    InfoRow(Icons.Outlined.Call, "Sabit / GSM: 0488 221 08 01  •  0553 072 08 01")
                    InfoRow(Icons.Outlined.Email, "E-posta: bilgi@zanabilisim.com")
                    InfoRow(Icons.Outlined.Schedule, "Çalışma Saatleri: Pzt – Cmt 09:00 – 19:00 (Pazar Kapalı)")
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = onOpenSite,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("zanabilisim.com")
                }
                FilledTonalButton(
                    onClick = onOpenStore,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("zanabilisim.com.tr")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ServicesScreen(
    services: List<ServiceItem>,
    isTablet: Boolean
) {
    if (isTablet) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(services) { item -> ServiceCard(item) }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(services) { item -> ServiceCard(item) }
        }
    }
}

@Composable
private fun ServiceCard(item: ServiceItem) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebPortalScreen(
    currentUrl: String,
    onSelectUrl: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = currentUrl == "https://www.zanabilisim.com",
                onClick = { onSelectUrl("https://www.zanabilisim.com") },
                label = { Text("Kurumsal / Servis") }
            )
            FilterChip(
                selected = currentUrl == "https://www.zanabilisim.com.tr",
                onClick = { onSelectUrl("https://www.zanabilisim.com.tr") },
                label = { Text("Mağaza / Yedek Parça") }
            )
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = WebViewClient()
                }
            },
            update = { webView ->
                if (webView.url != currentUrl) {
                    webView.loadUrl(currentUrl)
                }
            }
        )
    }
}

private fun dialPhone(context: Context, phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
}

private fun openWhatsApp(context: Context, phoneDigits: String) {
    val message = Uri.encode("Merhaba Zana Bilişim, destek almak istiyorum.")
    val uri = Uri.parse("https://wa.me/$phoneDigits?text=$message")
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}

private fun openMap(context: Context, address: String) {
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}
