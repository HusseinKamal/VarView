# VarView — Virtual Try-On App

A virtual clothing try-on Android application built with Jetpack Compose and SceneView. Users can visualize clothing items on a 3D avatar, customize body dimensions in real time, and view the result in augmented reality through their device camera.

## Features

- **3D Avatar Rendering** — Interactive 3D avatar displayed using SceneView (Filament engine)
- **Virtual Wardrobe** — Browse and select clothing items across categories (Shirts, T-Shirts, Trousers, Outerwear)
- **Body Profile Customization** — Adjust height, shoulder width, waist width, and chest width with real-time 3D updates
- **Full AR Mode** — Place the dressed avatar on real-world surfaces using ARCore plane detection
- **Material 3 UI** — Modern glass-morphism panels, animated transitions, and bottom navigation

## Architecture

The project follows **Clean Architecture** with clear layer separation:

```
com.hussein.varview/
├── domain/                          # Business logic (pure Kotlin)
│   ├── model/
│   │   ├── ClothingItem.kt          # Entity + Category enum
│   │   └── AvatarDimensions.kt      # Body measurements entity
│   ├── repository/
│   │   ├── WardrobeRepository.kt    # Interface
│   │   └── AvatarRepository.kt      # Interface
│   └── usecase/
│       ├── GetWardrobeItemsUseCase.kt
│       └── UpdateAvatarDimensionsUseCase.kt
├── data/                            # Data layer implementations
│   └── repository/
│       ├── WardrobeRepositoryImpl.kt
│       └── AvatarRepositoryImpl.kt
├── presentation/                    # UI layer (Compose + ViewModel)
│   ├── viewmodel/
│   │   └── TryOnViewModel.kt
│   ├── screen/
│   │   └── VFitScreen.kt
│   └── components/
│       ├── AvatarRenderer.kt        # 3D SceneView composable
│       ├── WardrobePanel.kt         # Clothing grid with category tabs
│       ├── BodyProfilePanel.kt      # Dimension sliders
│       └── FullARView.kt            # ARCore plane detection + model placement
└── MainActivity.kt
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| UI Framework | Jetpack Compose + Material 3 |
| 3D Rendering | SceneView 4.18.0 (Google Filament) |
| AR | ARCore via SceneView ARSceneView |
| State Management | Kotlin StateFlow + ViewModel |
| Navigation | Jetpack Navigation Compose |
| Architecture | Clean Architecture (Domain / Data / Presentation) |
| Language | Kotlin 2.2 |
| Build | Gradle KTS with Version Catalog |

## Requirements

- Android Studio Ladybug or newer
- Min SDK 24 (Android 7.0)
- Target SDK 36
- Device with ARCore support (for AR features, optional)

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/hussein/VarView.git
   cd VarView
   ```

2. Open in Android Studio and sync Gradle.

3. Add 3D model assets to `app/src/main/assets/models/`:
   - `avatar.glb` — Base humanoid avatar with skeleton
   - Clothing `.glb` files (see `assets/models/README.md` for full list)

4. (Optional) Add an HDR environment map to `app/src/main/assets/environments/`:
   - `studio_small.hdr` — For realistic lighting (download from [Poly Haven](https://polyhaven.com/hdris))

5. Build and run on a physical device.

## 3D Model Requirements

### Avatar
- Must have a proper skeleton/armature with named bones (Spine, Chest, Shoulder_L, Shoulder_R, Hips)
- Optional: include morph targets (blend shapes) for body type presets

### Clothing
- Upper body items (shirts, t-shirts, outerwear) should attach to the Chest bone
- Lower body items (trousers) should attach to the Hips bone
- For best results, clothing should share the same skeleton as the avatar

All models must be in `.glb` (binary glTF) format.

## How It Works

1. **SceneView Compose DSL** — The 3D scene is declared as composable functions. Nodes enter/exit the scene graph reactively based on state.

2. **Dimension Scaling** — A parent `Node` wraps the avatar and clothing with `Scale(x, y, z)` derived from the body profile sliders.

3. **Clothing Attachment** — Each clothing item is loaded as a `ModelNode` nested inside a positioned `Node` corresponding to its body attachment point.

4. **AR Placement** — `ARSceneView` detects horizontal planes. On detection, an `AnchorNode` is created at the plane's center and the dressed avatar is placed there.

## Permissions

| Permission | Purpose |
|-----------|---------|
| `CAMERA` | Required for AR mode |

AR is declared as `android:required="false"` so the app installs on devices without ARCore — the AR button simply won't function.

## License

This project is for educational and demonstration purposes.
