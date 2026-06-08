# 3D Model Assets

Place your `.glb` model files here. The app loads them from this directory.

## Required: Default Avatar

**File:** `avatar.glb`

This is the main humanoid avatar displayed in the app. You need a rigged humanoid model.

### Free options to download:

1. **Ready Player Me** (https://readyplayer.me) — Create a free avatar, export as .glb
2. **Mixamo** (https://www.mixamo.com) — Free characters from Adobe, export as FBX then convert to .glb
3. **Sketchfab** (https://sketchfab.com/search?type=models&q=humanoid+avatar&licenses=322a749bcfa841bab4000e7b753f4da0) — Search for CC-licensed humanoid models
4. **glTF Sample Models** (https://github.com/KhronosGroup/glTF-Sample-Models) — Test with CesiumMan or similar

### Quick test with a sample model:
Download from: https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/main/2.0/CesiumMan/glTF-Binary/CesiumMan.glb
Rename to `avatar.glb` and place here.

## Clothing Items

Each clothing item is a separate .glb file placed on the avatar body:

| File | Category | Description |
|------|----------|-------------|
| `shirt_white.glb` | SHIRTS | White dress shirt |
| `shirt_blue.glb` | SHIRTS | Blue formal shirt |
| `tshirt_black.glb` | T_SHIRTS | Black t-shirt |
| `tshirt_red.glb` | T_SHIRTS | Red graphic tee |
| `jeans_dark.glb` | TROUSERS | Dark denim jeans |
| `chinos_khaki.glb` | TROUSERS | Khaki chinos |
| `jacket_leather.glb` | OUTERWEAR | Leather jacket |
| `hoodie_grey.glb` | OUTERWEAR | Grey hoodie |

### Clothing Model Requirements

- Models should be positioned at origin (0,0,0)
- Upper body items (shirts/outerwear) center around chest height
- Lower body items (trousers) center around hip height
- For best results, clothing should share the same skeleton as the avatar
- Alternatively, static meshes work — the app positions them by category

## Environment (Optional)

The `environments/` folder can hold HDR files for lighting.
Without one, SceneView uses a default neutral environment.
