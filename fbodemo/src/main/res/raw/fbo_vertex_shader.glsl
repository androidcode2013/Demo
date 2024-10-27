attribute vec4 a_Position; // 顶点坐标

attribute vec2 a_TextureCoordinates;  //纹理坐标

varying vec2 aCoord;

void main(){
    gl_Position = a_Position;
    aCoord = a_TextureCoordinates;
}