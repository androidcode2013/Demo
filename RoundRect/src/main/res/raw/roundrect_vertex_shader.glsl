attribute vec4 a_Position;

varying vec2 vRectCoord;

void main()
{
    vRectCoord = vec2(a_Position.x, a_Position.y);
    gl_Position = a_Position;
}
