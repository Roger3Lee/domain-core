$bundleDir = "d:\work\code\domain\domain-core\build\central-bundle"
$zipPath = "d:\work\code\domain\domain-core\build\domain-core-3.0.0-bundle.zip"

if (Test-Path $zipPath) { Remove-Item $zipPath -Force }

# Create ZIP from the bundle directory (preserving relative paths)
Compress-Archive -Path (Join-Path $bundleDir "io") -DestinationPath $zipPath -Force

$zipInfo = Get-Item $zipPath
Write-Host "ZIP created: $($zipInfo.FullName) ($($zipInfo.Length) bytes)"