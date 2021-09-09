#extension GL_OES_EGL_image_external : require

precision mediump float; 
varying vec2 aCoord;

uniform samplerExternalOES  vTexture;  

void main(){
    
    vec4 rgba = texture2D(vTexture,aCoord);  //rgba
//    gl_FragColor = vec4(1.-rgba.r,1.-rgba.g,1.-rgba.b,rgba.a);
    gl_FragColor = rgba;
}
