precision highp float;

uniform sampler2D sourceImage;

varying vec2 vTextureCoord;

void main() {

    vec4 color = texture2D(sourceImage, vTextureCoord);
    gl_FragColor = color;
}