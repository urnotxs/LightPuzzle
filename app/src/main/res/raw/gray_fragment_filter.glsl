precision highp float;

uniform sampler2D sourceImage;

varying vec2 vTextureCoord;

void main() {

    vec4 color = texture2D(sourceImage, vTextureCoord);
    float rgb = color.g;
    vec4 c = vec4(rgb, rgb, rgb, color.a);
    gl_FragColor = c;
}