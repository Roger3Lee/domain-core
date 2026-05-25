$bundleDir = "d:\work\code\domain\domain-core\build\central-bundle"
if (Test-Path $bundleDir) { Remove-Item $bundleDir -Recurse -Force }
New-Item -ItemType Directory -Path $bundleDir -Force | Out-Null

$modules = @(
    @{name='domain-core'; pub='mavenJava'; artifactId='domain-core'},
    @{name='domain-dependencies'; pub='mavenPlatform'; artifactId='domain-dependencies'},
    @{name='domain-postgresql-support'; pub='mavenJava'; artifactId='domain-postgresql-support'}
)

function AddChecksums($filePath) {
    $md5 = [System.Security.Cryptography.MD5]::Create()
    $sha1 = [System.Security.Cryptography.SHA1]::Create()
    $bytes = [System.IO.File]::ReadAllBytes($filePath)
    $md5Hash = [BitConverter]::ToString($md5.ComputeHash($bytes)).Replace("-", "").ToLower()
    $sha1Hash = [BitConverter]::ToString($sha1.ComputeHash($bytes)).Replace("-", "").ToLower()
    $md5.Dispose()
    $sha1.Dispose()
    [System.IO.File]::WriteAllText($filePath + ".md5", $md5Hash)
    [System.IO.File]::WriteAllText($filePath + ".sha1", $sha1Hash)
}

foreach ($mod in $modules) {
    $destDir = Join-Path $bundleDir "io\github\roger3lee\$($mod.artifactId)\3.0.0"
    New-Item -ItemType Directory -Path $destDir -Force | Out-Null

    # Copy JARs + .asc signatures from build/libs
    $libsDir = "$($mod.name)\build\libs"
    if (Test-Path $libsDir) {
        Get-ChildItem $libsDir -Filter "*.jar" | ForEach-Object {
            $destFile = Join-Path $destDir $_.Name
            Copy-Item $_.FullName $destFile -Force
            Write-Host "  JAR: $($_.Name)"
            AddChecksums $destFile
        }
        Get-ChildItem $libsDir -Filter "*.asc" | ForEach-Object {
            $destFile = Join-Path $destDir $_.Name
            Copy-Item $_.FullName $destFile -Force
            Write-Host "  ASC: $($_.Name)"
            AddChecksums $destFile
        }
    }

    # Copy POM + .asc from publications
    $pubDir = "$($mod.name)\build\publications\$($mod.pub)"
    $pomFile = Join-Path $pubDir "pom-default.xml"
    if (Test-Path $pomFile) {
        $destPom = Join-Path $destDir "$($mod.artifactId)-3.0.0.pom"
        Copy-Item $pomFile $destPom -Force
        Write-Host "  POM: $($mod.artifactId)-3.0.0.pom"
        AddChecksums $destPom
    }
    $pomAsc = Join-Path $pubDir "pom-default.xml.asc"
    if (Test-Path $pomAsc) {
        $destPomAsc = Join-Path $destDir "$($mod.artifactId)-3.0.0.pom.asc"
        Copy-Item $pomAsc $destPomAsc -Force
        Write-Host "  POM.ASC: $($mod.artifactId)-3.0.0.pom.asc"
        AddChecksums $destPomAsc
    }
}

Write-Host ""
Write-Host "=== Bundle contents ==="
Get-ChildItem $bundleDir -Recurse -File | ForEach-Object {
    Write-Host $_.FullName.Replace($bundleDir + "\", "")
}
