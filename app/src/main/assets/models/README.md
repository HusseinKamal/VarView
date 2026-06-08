# 3D Model Assets

Place your `.glb` or `.gltf` model files here.

## Required Files

### Avatar
- `avatar.glb` — The base humanoid avatar with a proper skeleton (armature)
  - Must have named bones: Spine, Chest, Shoulder_L, Shoulder_R, Hips, etc.
  - Optional: Include morph targets (blend shapes) for "Heavy", "Tall", "Slim" presets.

### Clothing Items
- `shirt_white.glb`
- `shirt_blue.glb`
- `tshirt_black.glb`
- `tshirt_red.glb`
- `jeans_dark.glb`
- `chinos_khaki.glb`
- `jacket_leather.glb`
- `hoodie_grey.glb`

### Clothing Rigging Requirements
- Clothing models should share the same skeleton as the avatar for proper skinning.
- Alternatively, clothing can be "static" meshes parented to specific bones.
- Upper body items (shirts, t-shirts, outerwear) attach to the Chest bone.
- Lower body items (trousers) attach to the Hips bone.
