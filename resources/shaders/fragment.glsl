#version 330 core

in vec3 fColor;
in vec2 fTexCoord;

out vec4 oColor;

uniform sampler2D texture0;

void main() {
    oColor = texture(texture0, fTexCoord);
}
