package com.kseniabl.petsapp.ui

import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.kseniabl.petsapp.PetsAppTheme
import com.kseniabl.petsapp.R
import com.kseniabl.petsapp.utils.Routes
import com.kseniabl.petsapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PetsAppTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomBarView(navController, BottomBarItem.values()) },
                    topBar = { TopBar(viewModel) },
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    NavHost(navController = navController, startDestination = BottomBarItem.WALLET.route) {
                        composable(BottomBarItem.WALLET.route) {
                            WalletScreen(viewModel)
                        }
                        composable(BottomBarItem.TRIP.route) {
                            TripScreen(
                                viewModel = viewModel,
                                onProfileClicked = {
                                    navController.navigate(Routes.Profile.route)
                                }
                            )
                        }
                        composable(BottomBarItem.SHOP.route) {

                        }
                        composable(Routes.Profile.route) {

                        }
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        viewModel.cancelTimer()
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveTime()
    }

    override fun onStart() {
        super.onStart()
        viewModel.readTime()
    }
}

@Composable
fun BottomBarView(navController: NavController, items: Array<BottomBarItem>) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination?.route ?: BottomBarItem.WALLET
    BottomNavigation(
        modifier = Modifier
    ) {

        items.forEach { item ->
            val selected = item.route == currentDestination
            BottomNavigationItem(
                selected = selected,
                icon = { Icon(painterResource(item.icon), contentDescription = null) },
                onClick = {
                    if (item.route != currentDestination)
                        navController.navigate(item.route)
                },
            )
        }
    }
}

@Composable
fun WalletScreen(viewModel: MainViewModel) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .padding(16.dp),
        backgroundColor = MaterialTheme.colors.surface) {
            Column {
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 16.dp),
                    text = "Your Wallet",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold
                )
                WalletCardView()
                Spacer(modifier = Modifier.height(8.dp))
                WalletActions()
                Spacer(modifier = Modifier.height(16.dp))
                // TODO: redo viewmodel to remember
                TransactionsHistory(viewModel.transactions)
            }
    }
}

@Composable
fun TransactionsHistory(transactions: List<TransactionItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Transactions",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(transactions) { item ->
                TransactionItemView(item)
            }
        }
    }
}

@Composable
fun TransactionItemView(item: TransactionItem) {
    val formatter = SimpleDateFormat("dd:MM:YYYY")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = item.date
    val date = formatter.format(calendar.time)

    val plusOrMinus =
        if (item.type == TransactionsTypes.WITHDRAWAL) "-"
        else "+"
    val textColor =
        if (item.type == TransactionsTypes.WITHDRAWAL) Color(0xFFF44336)
        else Color(0xFF4CAF50)

    Row(modifier = Modifier) {
        Box(modifier = Modifier
            .size(46.dp)
            .aspectRatio(1f)
            .background(item.type.color, shape = CircleShape),
            contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = item.type.image),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text (
                modifier = Modifier.alpha(0.5F),
                text = date
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "$plusOrMinus ${item.sum}",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun WalletActions() {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        Column(modifier = Modifier) {
            Box(modifier = Modifier
                .size(50.dp)
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.top_arrow),
                    contentDescription = null
                )
            }
            Text(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp),
                text = "Send")
        }
        Spacer(modifier = Modifier.width(22.dp))
        Column(modifier = Modifier) {
            Box(modifier = Modifier
                .size(50.dp)
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
                .background(MaterialTheme.colors.secondary, shape = CircleShape),
                contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = null
                )
            }
            Text(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp),
                text = "Add")
        }
        Spacer(modifier = Modifier.width(22.dp))
        Column(modifier = Modifier) {
            Box(modifier = Modifier
                .size(50.dp)
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
                .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.bottom_arrow),
                    contentDescription = null
                )
            }
            Text(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp),
                text = "Receive")
        }
    }
}

@Composable
fun WalletCardView() {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Column(modifier = Modifier
            .padding(start = 24.dp, top = 16.dp, end = 16.dp, bottom = 24.dp)) {

            Text("Total balance", Modifier.alpha(0.5F), style = MaterialTheme.typography.subtitle2)
            Row(
                Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = "0.188",
                    style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = "Gem",
                    style = MaterialTheme.typography.body2
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "VJA4N7-SH2IMA-L82N3A-PQZ8N0",
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripScreen(
    viewModel: MainViewModel,
    onProfileClicked: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.statusBarsPadding(),
        backgroundColor = MaterialTheme.colors.surface
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            InventoryView()
            Spacer(Modifier.height(16.dp))
            MapView()
        }
    }
}

@Composable
fun TopBar(viewModel: MainViewModel) {
    Box(modifier = Modifier
        .padding(6.dp)) {
        Row(
            Modifier.fillMaxWidth()
        ) {
            val textState by viewModel.time.collectAsState()

            Image(
                painter = painterResource(id = R.drawable.flash),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .align(Alignment.CenterVertically),
                text = textState,
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.icon_dog),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.DarkGray, CircleShape),
                contentDescription = null
            )
        }
    }
}

@Composable
fun MapView() {
    val pos = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pos, 10f)
    }
    GoogleMap(
        cameraPositionState = cameraPositionState
    ) {
        Marker(state = MarkerState(position = pos))
    }
}

@Composable
fun InventoryView() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        items(3) {
            Image(
                modifier = Modifier.size(58.dp),
                painter = painterResource(id = R.drawable.bone),
                contentDescription = null
            )
        }
    }
}