const fs = require('fs');
const { execSync } = require('child_process');

// Generate SVG representing the exact Nutrix circular logo
const svgContent = `<?xml version="1.0" encoding="UTF-8"?>
<svg width="512" height="512" viewBox="0 0 512 512" fill="none" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <!-- Background foliage dark green gradient -->
    <radialGradient id="deepGreenBg" cx="30%" cy="30%" r="70%">
      <stop offset="0%" stop-color="#0E5A35" />
      <stop offset="50%" stop-color="#083820" />
      <stop offset="100%" stop-color="#041E11" />
    </radialGradient>

    <!-- Top Right Dominant Leaf Gradient -->
    <linearGradient id="rightLeafGrad" x1="180" y1="260" x2="440" y2="60" gradientUnits="userSpaceOnUse">
      <stop offset="0%" stop-color="#0B4D2C" />
      <stop offset="35%" stop-color="#147A46" />
      <stop offset="70%" stop-color="#1F9E5C" />
      <stop offset="100%" stop-color="#0F5E36" />
    </linearGradient>

    <!-- Center Upright Leaf Gradient -->
    <linearGradient id="centerLeafGrad" x1="256" y1="280" x2="256" y2="100" gradientUnits="userSpaceOnUse">
      <stop offset="0%" stop-color="#0D532F" />
      <stop offset="50%" stop-color="#239657" />
      <stop offset="100%" stop-color="#3CD07E" />
    </linearGradient>

    <!-- Left Leaf Gradient -->
    <linearGradient id="leftLeafGrad" x1="250" y1="260" x2="110" y2="160" gradientUnits="userSpaceOnUse">
      <stop offset="0%" stop-color="#0D4F2D" />
      <stop offset="60%" stop-color="#1E8B4F" />
      <stop offset="100%" stop-color="#32B369" />
    </linearGradient>

    <!-- Outer Green Crescent Rim Gradient -->
    <linearGradient id="rimGrad" x1="60" y1="60" x2="450" y2="450" gradientUnits="userSpaceOnUse">
      <stop offset="0%" stop-color="#083D21" />
      <stop offset="50%" stop-color="#052816" />
      <stop offset="100%" stop-color="#02140A" />
    </linearGradient>

    <!-- Soil Terracotta Warm Gradient -->
    <linearGradient id="soilWarmGrad" x1="80" y1="280" x2="420" y2="440" gradientUnits="userSpaceOnUse">
      <stop offset="0%" stop-color="#782E0C" />
      <stop offset="35%" stop-color="#A54B1A" />
      <stop offset="65%" stop-color="#D16E28" />
      <stop offset="85%" stop-color="#B3561A" />
      <stop offset="100%" stop-color="#642408" />
    </linearGradient>

    <!-- Soil Dark Loam Gradient -->
    <linearGradient id="soilDarkGrad" x1="120" y1="360" x2="380" y2="490" gradientUnits="userSpaceOnUse">
      <stop offset="0%" stop-color="#441C0C" />
      <stop offset="50%" stop-color="#270F06" />
      <stop offset="100%" stop-color="#140602" />
    </linearGradient>

    <!-- Glow/Highlight on upper right leaf -->
    <radialGradient id="leafHighlight" cx="340" cy="150" r="140" gradientUnits="userSpaceOnUse">
      <stop offset="0%" stop-color="#66D896" stop-opacity="0.35" />
      <stop offset="60%" stop-color="#239657" stop-opacity="0.1" />
      <stop offset="100%" stop-color="#000000" stop-opacity="0" />
    </radialGradient>

    <!-- Clip Path for Circular Emblem -->
    <clipPath id="circleClip">
      <circle cx="256" cy="256" r="236" />
    </clipPath>
  </defs>

  <!-- Circular Base Container -->
  <g clip-path="url(#circleClip)">
    <!-- Base Background Deep Forest Green -->
    <circle cx="256" cy="256" r="236" fill="url(#deepGreenBg)" />

    <!-- Left Outer Foliage Curve -->
    <path d="M256,20 C180,20 80,90 40,190 C20,240 25,300 50,340 C40,280 55,200 110,130 C160,70 215,35 256,20 Z" fill="url(#rimGrad)" />

    <!-- Top-Right Dominant Foliage Arc -->
    <path d="M256,20 C340,20 440,80 480,180 C505,245 495,310 465,360 C490,290 480,200 425,120 C375,55 315,28 256,20 Z" fill="url(#rimGrad)" />

    <!-- Top Right Large Leaf -->
    <path d="M254,265 C250,220 270,165 310,115 C355,60 410,32 445,40 C448,80 435,145 390,205 C345,265 295,285 254,265 Z" fill="url(#rightLeafGrad)" />
    <!-- Leaf highlight overlay -->
    <path d="M275,230 C290,170 330,110 385,70 C420,50 435,55 435,70 C410,120 370,180 320,230 C295,250 280,245 275,230 Z" fill="url(#leafHighlight)" />

    <!-- Center Upright Leaf -->
    <path d="M252,275 C242,225 240,165 248,115 C252,90 256,70 258,72 C262,95 270,145 268,205 C266,245 260,270 252,275 Z" fill="url(#centerLeafGrad)" />

    <!-- Left Curved Leaf -->
    <path d="M250,270 C220,250 165,220 125,185 C95,155 92,130 96,132 C115,145 155,175 195,215 C225,245 245,265 250,270 Z" fill="url(#leftLeafGrad)" fill-opacity="0.9" />
    <path d="M250,270 C210,235 150,195 105,155 C92,142 90,135 96,132 C125,125 175,155 220,205 C240,230 248,255 250,270 Z" fill="url(#leftLeafGrad)" />

    <!-- Lower Half Soil Layer - Dark Loam Deep Base -->
    <path d="M20,256 C20,386 125,492 256,492 C387,492 492,386 492,256 C460,330 380,360 300,345 C220,330 140,310 20,256 Z" fill="url(#soilDarkGrad)" />

    <!-- Lower Half Soil Layer - Terracotta / Copper Warm Wave -->
    <path d="M20,260 C60,290 140,315 220,330 C300,345 390,325 492,256 C492,330 440,430 360,470 C280,485 180,465 100,420 C50,380 25,320 20,260 Z" fill="url(#soilWarmGrad)" />

    <!-- Subtle texture curves in soil -->
    <path d="M60,320 C140,370 260,380 370,350 C420,335 460,305 485,275 C450,360 380,430 300,455 C200,465 110,410 60,320 Z" fill="#200C05" fill-opacity="0.45" />
    <path d="M110,375 C190,420 300,425 400,380 C360,440 280,475 210,470 C150,450 115,415 110,375 Z" fill="#130602" fill-opacity="0.6" />

    <!-- Pure White Elegant Stem and Negative Space Contours -->
    <!-- Center to Left leaf divider -->
    <path d="M256,285 C250,235 215,190 150,140 C125,120 100,130 96,134 C105,145 135,175 175,215 C215,255 245,280 256,285 Z" fill="#FFFFFF" />

    <!-- Center leaf spine and right separator -->
    <path d="M256,285 C256,210 258,140 256,80 C258,80 262,130 265,190 C268,230 266,270 256,285 Z" fill="#FFFFFF" />

    <!-- Right leaf sweeping white stem -->
    <path d="M256,285 C270,240 310,180 365,125 C410,80 440,48 443,44 C440,55 410,110 360,180 C315,245 275,275 256,285 Z" fill="#FFFFFF" />

    <!-- Central stem trunk descending into soil -->
    <path d="M253,275 L259,275 L258,345 L254,345 Z" fill="#FFFFFF" />

    <!-- ==================== NUTRIX TYPOGRAPHY ==================== -->
    <!-- Distinctive stylized white lettering integrated into the soil bed -->

    <!-- Letter 'N' -->
    <!-- Left outer curving stem flowing from leaf border -->
    <path d="M68,290 C75,320 78,360 80,420 L92,420 C90,360 86,325 78,290 Z" fill="#FFFFFF" />
    <!-- Diagonal and Right stem of N -->
    <path d="M78,295 L135,395 L135,310 L146,310 L146,425 L132,425 L75,320 Z" fill="#FFFFFF" />

    <!-- Letter 'U' -->
    <path d="M160,320 L172,320 L172,395 C172,418 185,428 202,428 C218,428 230,418 230,395 L230,320 L242,320 L242,396 C242,426 224,440 202,440 C178,440 160,426 160,396 Z" fill="#FFFFFF" />

    <!-- Letter 'T' -->
    <!-- Horizontal Crossbar -->
    <path d="M245,335 L295,335 L295,347 L276,347 L276,445 C276,470 266,488 252,492 C255,485 264,468 264,445 L264,347 L245,347 Z" fill="#FFFFFF" />

    <!-- Letter 'R' -->
    <!-- Main stem and loop -->
    <path d="M305,340 L345,340 C365,340 376,352 376,368 C376,384 364,395 345,395 L317,395 L317,438 L305,438 Z M317,352 L317,383 L342,383 C356,383 363,377 363,368 C363,358 356,352 342,352 Z" fill="#FFFFFF" />
    <!-- R Kick leg swooping down -->
    <path d="M338,392 L372,442 L388,442 L350,390 Z" fill="#FFFFFF" />

    <!-- Letter 'I' -->
    <path d="M388,342 L400,342 L400,442 L388,442 Z" fill="#FFFFFF" />

    <!-- Letter 'X' -->
    <!-- Diagonal stroke 1 (top-left to bottom-right) -->
    <path d="M410,344 L465,440 L452,440 L398,344 Z" fill="#FFFFFF" />
    <!-- Diagonal stroke 2 (bottom-left to top-right) -->
    <path d="M400,438 L458,344 L470,344 L412,438 Z" fill="#FFFFFF" />

    <!-- Circular Outer Border Ring -->
    <circle cx="256" cy="256" r="232" stroke="#062916" stroke-width="8" fill="none" />
  </g>
</svg>
`;

fs.writeFileSync('/tmp/nutrix_logo.svg', svgContent);
console.log('SVG generated at /tmp/nutrix_logo.svg');

// Render SVG to 512x512 PNG using ImageMagick
execSync('convert -background none /tmp/nutrix_logo.svg -resize 512x512 /tmp/nutrix_logo.png');
console.log('PNG generated at /tmp/nutrix_logo.png');
