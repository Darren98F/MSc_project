attribute vec4 vPosition; 

attribute vec2 vCoord;  //Texture coordinates

varying vec2 aCoord;

void main(){
    
    gl_Position = vPosition;
    aCoord = vCoord;
}
