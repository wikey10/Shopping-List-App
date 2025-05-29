package com.app.myshoppingapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int,var name:String, var quantity:Int,var isEditing:Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(modifier: Modifier = Modifier){

    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(12.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Shopping List")
        Button(onClick = {
            showDialog=true
        },
            colors = ButtonColors(
                containerColor = Color.Blue,
                contentColor = Color.White,
                disabledContainerColor = Color.Blue,
                disabledContentColor = Color.White
            )
        ) {
            Text("Add Item")
        }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)){
            items(sItems){
               item->
                if(item.isEditing){
                    ShoppingItemEditor(item, onEditComplete = {
                        editedName,editedQuantity ->
                        sItems = sItems.map {
                            item.copy(isEditing = false) }
                        val editedItem = sItems.find {
                            it.id == item.id
                        }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                }
                else{
                   ShoppingListItem(item, onEditClick = {
                       //finding out which item we are editing and changing, isEditing =true
                       sItems =sItems.map { it.copy(isEditing =  it.id == item.id) }
                   },
                       onDeleteClick = {
                           sItems = sItems-item
                       }
                       )
                }
            }
        }
    }

    if(showDialog){
        BasicAlertDialog(
            onDismissRequest = {showDialog=false},
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White // Your background color
                )
            ) {
                Column(modifier = Modifier.padding(12.dp),) {
                    Text("Add Item")
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {
                            itemName=it
                        },
                        label = {
                            Text("Enter the Product")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(6.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {
                            itemQuantity=it
                        },
                        label = {
                            Text("Enter the Quantity")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(6.dp)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = {
                            showDialog=false
                        }) { Text("Cancel") }
                        Button(onClick = {
                            if(itemName.isNotEmpty()){
                                val newItem = ShoppingItem(
                                    id = sItems.size+1,
                                    name = itemName,
                                    quantity = itemQuantity.toInt()
                                    )
                                sItems = sItems+newItem
                                showDialog=false
                                itemName=""
                                itemQuantity=""
                            }
                        }) { Text("Add")}
                    }
                }
            }
        }
    }

}


@Composable
fun ShoppingItemEditor(item: ShoppingItem,onEditComplete: (String,Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier.fillMaxWidth().background(Color.White)
        .padding(8.dp,),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {
                    editedName=it
                },
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = {
                    editedQuantity=it
                },
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing=false
            onEditComplete(editedName,editedQuantity.toIntOrNull()?:1)
        },
            colors = ButtonColors(
                containerColor = Color.Blue,
                contentColor = Color.White,
                disabledContainerColor = Color.Blue,
                disabledContentColor = Color.White
            )
            ) {
            Text("Save")
        }
    }

}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit
){
    Row(
        modifier = Modifier.padding(8.dp).fillMaxWidth()
            .border(border = BorderStroke(width = 2.dp, color = Color.Blue),
                shape = RoundedCornerShape(20)
                ),
    ) {
        Text(item.name, modifier = Modifier.padding(8.dp))
        Text("QTY: "+item.quantity.toString(), modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp
            ).fillMaxWidth(),
            horizontalArrangement = Arrangement.End
            ) {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}