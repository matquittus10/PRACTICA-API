package com.example.practicaapi.ui.theme.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.practicaapi.model.AnimalExtinto
import com.example.practicaapi.model.Coctel
import com.example.practicaapi.model.PlatoComida
import com.example.practicaapi.ui.theme.PracticaAPITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    correoUsuario: String = "",
    onCerrarSesion: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Práctica de APIs",
                            fontWeight = FontWeight.Bold
                        )
                        if (correoUsuario.isNotEmpty()) {
                            Text(
                                text = correoUsuario,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                },
                actions = {
                    if (correoUsuario.isNotEmpty()) {
                        IconButton(onClick = onCerrarSesion) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Cerrar sesión"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Panel de Integración de APIs",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "A continuación se presentan las tarjetas para cada una de las APIs integradas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Tarjeta API 1: PlatoComida (TheMealDB)
            item {
                CardApiPlatoComida(
                    cargando = viewModel.cargando,
                    mensajeError = viewModel.mensajeError,
                    listaPlatos = viewModel.listaPlatos,
                    idsFavoritos = viewModel.idsFavoritos,
                    onToggleFavorito = { plato -> viewModel.alternarFavorito(plato) },
                    onReintentar = { viewModel.cargarComida() }
                )
            }

            // Tarjeta API 2: Coctel (TheCocktailDB)
            item {
                CardApiCoctel(
                    cargando = viewModel.cargandoCoctel,
                    mensajeError = viewModel.errorCoctel,
                    coctel = viewModel.coctel,
                    onCargarCoctel = { viewModel.cargarCoctel() }
                )
            }

            // Tarjeta API 3: AnimalExtinto (Extinct API)
            item {
                CardApiAnimal(
                    cargando = viewModel.cargandoAnimales,
                    mensajeError = viewModel.errorAnimales,
                    animal = viewModel.animal,
                    onCargarAnimal = { viewModel.cargarAnimales() }
                )
            }
        }
    }
}

@Composable
fun CardApiPlatoComida(
    cargando: Boolean,
    mensajeError: String?,
    listaPlatos: List<PlatoComida>,
    idsFavoritos: Set<String>,
    onToggleFavorito: (PlatoComida) -> Unit,
    onReintentar: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "API 1: Platos de Comida",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "TheMealDB (https://www.themealdb.com/)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                cargando -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                mensajeError != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: $mensajeError",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onReintentar) {
                            Text("Reintentar")
                        }
                    }
                }

                listaPlatos.isEmpty() -> {
                    Text(
                        text = "No se encontraron platos.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listaPlatos.take(5).forEach { plato ->
                            ItemPlatoComida(
                                plato = plato,
                                esFavorito = idsFavoritos.contains(plato.id),
                                onToggleFavorito = { onToggleFavorito(plato) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemPlatoComida(
    plato: PlatoComida,
    esFavorito: Boolean,
    onToggleFavorito: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plato.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onToggleFavorito) {
                    Text(
                        text = if (esFavorito) "❤️" else "🤍",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (plato.categoria.isNotBlank()) {
                    AssistChip(
                        onClick = { },
                        label = { Text(plato.categoria) }
                    )
                }
                if (plato.pais.isNotBlank()) {
                    AssistChip(
                        onClick = { },
                        label = { Text(plato.pais) }
                    )
                }
            }
            if (plato.instrucciones.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = plato.instrucciones,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CardApiCoctel(
    cargando: Boolean,
    mensajeError: String?,
    coctel: Coctel?,
    onCargarCoctel: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "API 2: Cóctel Aleatorio",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = "TheCocktailDB (https://www.thecocktaildb.com/)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                cargando -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                mensajeError != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: $mensajeError",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onCargarCoctel) {
                            Text("Reintentar")
                        }
                    }
                }

                coctel != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (coctel.imagen.isNotBlank()) {
                                AsyncImage(
                                    model = coctel.imagen,
                                    contentDescription = coctel.nombre,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = coctel.nombre,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (coctel.categoria.isNotBlank()) {
                                        AssistChip(
                                            onClick = { },
                                            label = { Text(coctel.categoria) }
                                        )
                                    }
                                    AssistChip(
                                        onClick = { },
                                        label = {
                                            Text(if (coctel.esAlcoholico) "Alcohólico" else "Sin alcohol")
                                        }
                                    )
                                }
                            }
                        }

                        if (coctel.instrucciones.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = coctel.instrucciones,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onCargarCoctel,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Otro cóctel")
                        }
                    }
                }

                else -> {
                    Text(
                        text = "No hay cóctel cargado.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun CardApiAnimal(
    cargando: Boolean,
    mensajeError: String?,
    animal: AnimalExtinto?,
    onCargarAnimal: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "API 3: Animal Extinto Aleatorio",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Extinct API (https://extinct-api.herokuapp.com/)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                cargando -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                mensajeError != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: $mensajeError",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onCargarAnimal) {
                            Text("Reintentar")
                        }
                    }
                }

                animal != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ItemAnimalExtinto(animal = animal)

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onCargarAnimal,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Otro animal")
                        }
                    }
                }

                else -> {
                    Text(
                        text = "No hay animales extintos registrados.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ItemAnimalExtinto(animal: AnimalExtinto) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = animal.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (animal.nombreCientifico.isNotBlank()) {
                Text(
                    text = animal.nombreCientifico,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (animal.ubicacion.isNotBlank()) {
                    AssistChip(
                        onClick = { },
                        label = { Text(animal.ubicacion) }
                    )
                }
                if (animal.ultimoRegistro.isNotBlank()) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Año: ${animal.ultimoRegistro}") }
                    )
                }
            }
            if (animal.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Descripción:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = animal.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    PracticaAPITheme {
        HomeView()
    }
}
