# Habilitar TLS 1.2 y TLS 1.3 en PowerShell para evitar errores de conexión SSL
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12 -bor [Net.SecurityProtocolType]::Tls13

New-Item -ItemType Directory -Force -Path "images"

$pilotos = @{
    "1" = "https://upload.wikimedia.org/wikipedia/commons/8/89/Max_Verstappen_2023_Austria_FP2_%28cropped%29.jpg"
    "2" = "https://upload.wikimedia.org/wikipedia/commons/4/4e/Sergio_Perez_2022_Imola.jpg"
    "3" = "https://upload.wikimedia.org/wikipedia/commons/8/87/Lewis_Hamilton_2022_Imola.jpg"
    "4" = "https://upload.wikimedia.org/wikipedia/commons/3/3d/George_Russell_2022_Imola.jpg"
    "5" = "https://upload.wikimedia.org/wikipedia/commons/5/52/Charles_Leclerc_2022_Imola_%28cropped%29.jpg"
    "6" = "https://upload.wikimedia.org/wikipedia/commons/a/af/Carlos_Sainz_Jr._2022_Imola.jpg"
    "7" = "https://upload.wikimedia.org/wikipedia/commons/5/50/Lando_Norris_2022_Imola.jpg"
    "8" = "https://upload.wikimedia.org/wikipedia/commons/4/4b/Oscar_Piastri_2023.jpg"
    "9" = "https://upload.wikimedia.org/wikipedia/commons/8/85/Fernando_Alonso_2022_Imola.jpg"
    "10" = "https://upload.wikimedia.org/wikipedia/commons/7/75/Lance_Stroll_2022_Imola.jpg"
    "11" = "https://upload.wikimedia.org/wikipedia/commons/2/2a/Esteban_Ocon_2022_Imola.jpg"
    "12" = "https://upload.wikimedia.org/wikipedia/commons/b/b3/Pierre_Gasly_2022_Imola.jpg"
    "13" = "https://upload.wikimedia.org/wikipedia/commons/c/c5/Valtteri_Bottas_2022_Imola_%28cropped%29.jpg"
    "14" = "https://upload.wikimedia.org/wikipedia/commons/5/5b/Zhou_Guanyu_2022_Imola_%28cropped%29.jpg"
    "15" = "https://upload.wikimedia.org/wikipedia/commons/1/1a/Kevin_Magnussen_2022_Imola.jpg"
    "16" = "https://upload.wikimedia.org/wikipedia/commons/4/4f/Nico_H%C3%BClkenberg_2020.jpg"
    "17" = "https://upload.wikimedia.org/wikipedia/commons/0/00/Yuki_Tsunoda_2022_Imola.jpg"
    "18" = "https://upload.wikimedia.org/wikipedia/commons/8/87/Daniel_Ricciardo_2022_Imola.jpg"
    "19" = "https://upload.wikimedia.org/wikipedia/commons/7/72/Alexander_Albon_2022_Imola.jpg"
    "20" = "https://upload.wikimedia.org/wikipedia/commons/0/07/Logan_Sargeant_2023_F1_United_States_GP.jpg"
}

$headers = @{
    "User-Agent" = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

foreach ($key in $pilotos.Keys) {
    $url = $pilotos[$key]
    $dest = "images/$key.jpg"
    Write-Host "Downloading Pilot $key..."
    try {
        Invoke-WebRequest -Uri $url -OutFile $dest -Headers $headers -TimeoutSec 10 -ErrorAction Stop
    } catch {
        Write-Warning "Fallo piloto ${key}: $_"
    }
}

# Circuits
$circuitos = @{
    "monaco" = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Monte_Carlo_Formula_1_track_map.svg/320px-Monte_Carlo_Formula_1_track_map.svg.png"
    "silverstone" = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Silverstone_Circuit_2020_layout.png/320px-Silverstone_Circuit_2020_layout.png"
    "spa" = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/Spa-Francorchamps_of_Belgium.svg/320px-Spa-Francorchamps_of_Belgium.svg.png"
    "monza" = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Monza_of_Italy.svg/320px-Monza_of_Italy.svg.png"
    "suzuka" = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Suzuka_of_Japan.svg/320px-Suzuka_of_Japan.svg.png"
}

foreach ($key in $circuitos.Keys) {
    $url = $circuitos[$key]
    $dest = "images/$key.png"
    Write-Host "Downloading Circuit $key..."
    try {
        Invoke-WebRequest -Uri $url -OutFile $dest -Headers $headers -TimeoutSec 10 -ErrorAction Stop
    } catch {
        Write-Warning "Fallo circuito ${key}: $_"
    }
}

Write-Host "All downloads done!"
