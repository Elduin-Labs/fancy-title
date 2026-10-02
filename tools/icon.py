"""Draws the mod icon: big and little sparkles on a night-blue background. python3 tools/icon.py"""
import os, struct, zlib

def write_png(path, px, w, h):
    raw = b"".join(b"\x00" + bytes(c for p in px[y * w:(y + 1) * w] for c in p) for y in range(h))
    def chunk(k, d): return struct.pack(">I", len(d)) + k + d + struct.pack(">I", zlib.crc32(k + d) & 0xFFFFFFFF)
    open(path, "wb").write(b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, 6, 0, 0, 0))
                           + chunk(b"IDAT", zlib.compress(raw, 9)) + chunk(b"IEND", b""))

n = 32
px = [(0, 0, 0, 0)] * (n * n)
for y in range(n):
    for x in range(n):
        corner = min(x, n - 1 - x) + min(y, n - 1 - y)
        if corner < 2: continue
        edge = x in (0, n - 1) or y in (0, n - 1) or corner == 2
        px[y * n + x] = (34, 30, 80, 255) if edge else (52, 46, 120, 255)

def star(cx, cy, arm, core, glow):
    px[cy * n + cx] = core
    for i in range(1, arm + 1):
        c = core if i <= arm // 2 else glow
        for (x, y) in ((cx + i, cy), (cx - i, cy), (cx, cy + i), (cx, cy - i)):
            if 0 <= x < n and 0 <= y < n: px[y * n + x] = c
    for (x, y) in ((cx + 1, cy + 1), (cx - 1, cy - 1), (cx + 1, cy - 1), (cx - 1, cy + 1)):
        px[y * n + x] = glow

star(15, 15, 9, (255, 246, 170, 255), (255, 214, 90, 255))
star(6, 6, 3, (255, 255, 255, 255), (168, 232, 255, 255))
star(25, 7, 2, (255, 255, 255, 255), (255, 200, 240, 255))
star(24, 24, 3, (255, 255, 255, 255), (168, 232, 255, 255))
star(7, 25, 2, (255, 255, 255, 255), (255, 200, 240, 255))
big = [px[(y // 8) * n + x // 8] for y in range(n * 8) for x in range(n * 8)]
write_png(os.path.join(os.path.dirname(__file__), "..", "src", "main", "resources", "assets", "icon.png"), big, n * 8, n * 8)
print("done")
