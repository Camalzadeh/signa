<p align="center">
  <img src="docs/logo.png" width="128" alt="Signa Logo">
</p>

<h1 align="center"><strong>Signa</strong> - Real-time Signal Intelligence & Security</h1>

<p align="center">
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.1.0-blue.svg?style=flat&logo=kotlin" alt="Kotlin"></a>
  <a href="https://www.jetbrains.com/lp/compose-multiplatform/"><img src="https://img.shields.io/badge/Compose%20Multiplatform-1.7.1-orange.svg?style=flat&logo=jetpackcompose" alt="Compose Multiplatform"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-BSD%203--Clause-green.svg" alt="License"></a>
  <a href="https://kotlinconf.com"><img src="https://img.shields.io/badge/KotlinConf-Contest-purple.svg" alt="KotlinConf Contest"></a>
</p>

---

## Android Demo Video

<p align="center">
  <video src="https://github.com/user-attachments/assets/d30bcb5e-b677-4791-ad4a-fc992113e433
" width="320" controls>
  </video>
</p>

---

**Signa** is a powerful, cross-platform signal monitoring and analysis tool built with **Kotlin Multiplatform**. It allows users to detect, track, and analyze surrounding radio signals in real-time, providing deep insights and security audits powered by **Gemini AI**.

Developed for the **KotlinConf Contest**, Signa demonstrates the power of modern Kotlin development by delivering a high-performance, beautiful UI experience across **Android, Windows, macOS, and Linux**.

---

## Demo Videos

Experience Signa in action across different platforms:

| Platform | Video Link |
| :--- | :--- |
| **Android** | [Watch Demo](https://drive.google.com/file/d/1OA3o3yQykAlOp_CjbjrDYKeYCCLpPviO/view?usp=drive_link) |
| **Windows** | [Watch Demo](https://drive.google.com/file/d/1JD1590c8OYlJHVHiasYhlQdU9yy22eii/view?usp=drive_link) |
| **Full Folder** | [Google Drive Folder](https://drive.google.com/drive/folders/1zjLoIuF3QCKfW6XrGXiOW52zIDFiExxp?usp=drive_link) |

---

## Latest Releases

Download the latest version of Signa for your platform:

| Platform | Download Link |
| :--- | :--- |
| **Android** | [Download APK](https://github.com/Camalzadeh/signa/releases/download/v2026.01.11-2253/composeApp-release.apk) |
| **Windows** | [Download EXE](https://github.com/Camalzadeh/signa/releases/download/v2026.01.11-2253/org.signa.app-1.0.0.msi) |
| **macOS** | [Download DMG](https://github.com/Camalzadeh/signa/releases/download/v2026.01.11-2253/org.signa.app-1.0.0.dmg) |
| **Linux** | [Download DEB](https://github.com/Camalzadeh/signa/releases/download/v2026.01.11-2253/org.signa.app_1.0.0-1_amd64.deb) |

> [!TIP]
> You can find all versions and release notes in the [GitHub Releases](https://github.com/Camalzadeh/signa/releases) page.

---

## Key Features

- **Real-time Signal Scanning**: Monitor environmental signals as they happen.
- **AI-Powered Security Analysis**: Integrated **Gemini AI** to analyze signal risks and detect potential threats (hidden cameras, suspicious trackers, etc.).
- **Interactive Data Visualization**: Smooth, interactive charts to analyze signal strength and frequency history.
- **Local Persistence**: Full **Room Database** integration to store and manage signal history offline.
- **Smart Filtering & Sorting**: Easily find specific signals based on type, strength, or security status.
- **Premium UI/UX**: Built with **Compose Multiplatform** for a fluid, modern interface that adapts to mobile and desktop.

---

## Tech Stack

Signa is built using the latest industry standards for Kotlin development:

- **Language**: [Kotlin](https://kotlinlang.org/) (KMP)
- **UI Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- **Architecture**: Clean Architecture (Data, Domain, UI layers)
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Database**: [Room KMP](https://developer.android.com/kotlin/multiplatform/room)
- **Networking**: [Ktor Client](https://ktor.io/)
- **AI Integration**: [Google Gemini AI](https://ai.google.dev/) (Direct AI Integration)
- **Concurrency**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)

---

## Architecture Diagram

Signa follows **Clean Architecture** principles to maintain a separation of concerns and ensure the codebase remains scalable and testable.

<p align="center">
  <img src="docs/architecture_diagram.png" alt="Signa Architecture Diagram" width="800">
</p>

---

## Screenshots

### Android
<p align="center">
  <img src="docs/android-photos/android_screen_1.jpeg" width="200">
  <img src="docs/android-photos/android_screen_2.jpeg" width="200">
  <img src="docs/android-photos/android_screen_3.jpeg" width="200">
  <img src="docs/android-photos/android_screen_7.jpeg" width="200">
</p>

### Windows
<p align="center">
  <img src="docs/windows-photos/windows_screen_3.png" width="400">
  <img src="docs/windows-photos/windows_screen_4.png" width="400">
</p>

---

## Getting Started

### Prerequisites
- JDK 17 or higher
- Android Studio Koala+ or IntelliJ IDEA
- Kotlin 2.1.0

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Camalzadeh/signa.git
   ```
2. Open in Android Studio or IntelliJ IDEA.
3. Provide your Gemini API Key in `local.properties`.
4. Build and run:
   - **Android**: Run the `composeApp` module on an emulator or device.
   - **Desktop**: Run the `run` task in the `composeApp` Gradle group.

---

## Application Essay

> [!NOTE]
> For a detailed look into the philosophy and technical inner workings of Signa, please refer to our full essay:
> **[Signa: Revolutionizing Signal Intelligence with Kotlin Multiplatform and AI](docs/essay.md)**

### Abstract
In the rapidly evolving landscape of wireless communications, the ability to monitor, analyze, and secure our surrounding signal environment has become paramount. **Signa** is a cutting-edge, cross-platform application designed to provide users with real-time signal intelligence... *(Read more in the [full essay](docs/essay.md))*

---

## License

This project is licensed under the BSD 3-Clause License - see the [LICENSE](LICENSE) file for details.

---

## KotlinConf Contest
Signa is proudly submitted as an entry for the **KotlinConf Contest**. It showcases the versatility of Kotlin Multiplatform in building security-focused, AI-integrated applications for the modern world.

Developed by **Humbat Jamalzadeh**.
