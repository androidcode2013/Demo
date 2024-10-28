precision mediump float;// 数据精度
varying vec2 aCoord;
uniform sampler2D  u_TextureUnit;

void main(){
    vec4 rgba = texture2D(u_TextureUnit, aCoord);//rgba
    gl_FragColor = rgba;
}