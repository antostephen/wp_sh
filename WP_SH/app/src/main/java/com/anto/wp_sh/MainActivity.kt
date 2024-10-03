package com.anto.wp_sh

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.anto.wp_sh.data.model.SchoolDetailsItem
import com.anto.wp_sh.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SchoolDetailsScreen(homeViewModel)
        }
        // Observe data from ViewModel
        homeViewModel.data.observe(this) { schoolData ->
            // Update UI with data
            Log.d("MyActivity", "Data received: $schoolData")
            schoolData.forEach{ school ->
                //UpdateSchoolDetailsScreen(homeViewModel)
            }
        }

        // Observe error from ViewModel
        homeViewModel.error.observe(this) { errorMessage ->
            Log.e("MyActivity", "Error: $errorMessage")
        }

        // Call the API via ViewModel
        homeViewModel.getSchoolDetails()
        println("<< onCreate")
    }

@Composable
fun UpdateSchoolDetailsScreen(school: SchoolDetailsItem, count: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Record Number : $count",
                style = MaterialTheme.typography.labelLarge)
            Text(text = "DBN: ${school.dbn}")
            Text(text = "School Name: ${school.schoolName}")
            Text(text = "Phone Number: ${school.phoneNumber}")
            Text(text = "City: ${school.city}")
            Text(text = "Borough: ${school.borough}")
            Text(text = "Zipcode: ${school.zip}")
            Text(text = "State Code: ${school.stateCode}")
            Text(text = "Website: ${school.website}")
            Text(text = "Location: ${school.location}")
            Text(text = "School Email: ${school.schoolEmail}")
        }
    }

}
    @Composable
    fun SchoolDetailsScreen(homeViewModel: HomeViewModel) {
        // Observe the LiveData from ViewModel as State
        val schoolData by homeViewModel.data.observeAsState(initial = emptyList())

        // Check if the data is not empty, then display it
        if (schoolData.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxSize()){

                Text(text = "NYC Schools Data",
                    modifier = Modifier.padding(20.dp),
                    style=MaterialTheme.typography.headlineLarge)

                LazyColumn {
                    items(schoolData.size) { index ->
                        UpdateSchoolDetailsScreen(schoolData[index], index)

                        Row(
                            modifier = Modifier.fillMaxWidth(), // Make the row take up the full width of the screen
                            horizontalArrangement = Arrangement.End // Align the content (button) to the right
                        ) {
                            Button(
                                modifier = Modifier.padding(5.dp),
                                onClick = {
                                }
                            ) {
                                // Content inside the Button
                                //Text(text = if (clicked) "Clicked!" else "Click Me")
                                Text(text = "More info...")
                            }
                        }

                        Divider(
                            color = Color.Gray,   // Set the color of the divider
                            thickness = 1.dp      // Set the thickness of the divider
                        )
                    }
                }
            }
        } else {
            // Display a loading state or placeholder when data is not available
            Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
                Text(text = "Loading...", style=MaterialTheme.typography.bodyLarge)
            }
        }
    }

}