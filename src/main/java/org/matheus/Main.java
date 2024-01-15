package org.matheus;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
public class Main{
    private static long window = 0;


    // Vertex Shader source code
// Vertex Shader
//    static String vertexShaderSource = "#version 330 core\n" +
//            "layout (location = 0) in vec3 aPos;\n" +
//            "uniform mat4 projectionMatrix;\n"+
//            "uniform mat4 modelMatrix;\n"+
//            "void main()\n" +
//            "{\n" +
//            "   gl_Position = projectionMatrix * modelMatrix * vec4(aPos, 5.0);\n" +
//            "}\0";
//    //Fragment Shader source code
//    static String fragmentShaderSource = "#version 330 core\n"+
//            "out vec4 FragColor;\n"+
//                        "void main()\n"+
//            "{\n"+
//            " vec3 gradientColor = vec3(0.0, gl_FragCoord.y / 600.0, 1.0 - gl_FragCoord.y / 600.0);\n"+
//            " FragColor = vec4(gradientColor, 1.0);"+
//                        "}\n\0";

    // Código dos shaders (substitua por seus shaders específicos)
    static final String vertexShaderSource = "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "uniform mat4 modelMatrix;\n" +
            "uniform mat4 projectionMatrix;\n" +
            "void main() {\n" +
            "gl_Position = projectionMatrix * modelMatrix * vec4(aPos, 1.0);\n" +
            "}";
    static final String fragmentShaderSource = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "void main() {\n" +
            "vec3 gradientColor = vec3(0.0, gl_FragCoord.y / 600.0, 1.0 - gl_FragCoord.y / 600.0);\n"+
            " FragColor = vec4(gradientColor, 1.0);"+
            "}";

    private static Matrix4f modelMatrix = new Matrix4f();


    public static void main(String[] args) {
        glfwSetErrorCallback((error, description) -> System.err.println("Erro GLFW " + error + ": " + description));
        glfwInit();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_DEPTH_BITS, 24);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        window = glfwCreateWindow(800,600,"Hello world",0,0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwSwapInterval(1);
        glEnable(GL_DEPTH_TEST);
        glViewport(0,0,800,600);

        float[] vertices = {
                // Vértices da face frontal
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,

                // Vértices da face traseira
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,

                // Vértices da face superior
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,

                // Vértices da face inferior
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,

                // Vértices da face esquerda
                -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,

                // Vértices da face direita
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
        };
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader,vertexShaderSource);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Erro na compilação do shader de vértices: " + glGetShaderInfoLog(vertexShader));
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader,fragmentShaderSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Erro na compilação do shader de fragmentos: " + glGetShaderInfoLog(fragmentShader));
        }
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Erro na linkagem do programa de shader: " + glGetProgramInfoLog(shaderProgram));
        }


        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        glBufferData(GL_ARRAY_BUFFER,vertices,GL_STATIC_DRAW);

        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);

        glEnableVertexAttribArray(0);
        // Configuração da matriz de projeção
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);

            // Aumente o valor do campo de visão (fovy)
            Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(100f), 800.0f / 600.0f, 0.1f, 500.0f);
            projectionMatrix.get(buffer);

            int projectionMatrixLocation = glGetUniformLocation(shaderProgram, "projectionMatrix");
            glUseProgram(shaderProgram);
            glUniformMatrix4fv(projectionMatrixLocation, false, buffer);
        }


        Matrix4f rotationMatrix = new Matrix4f();
        while(!glfwWindowShouldClose(window)){
            if(glfwGetKey(window,GLFW_KEY_ESCAPE) == GLFW_PRESS){
                glfwSetWindowShouldClose(window,true);
            }

            glEnable(GL_DEPTH_TEST); // Habilita o teste de profundidade
            glClearColor(0.0f,0.1f,0.25f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            // Calcula o ângulo de rotação baseado no tempo
            float rotationAngle = (float) Math.toRadians(glfwGetTime() * 60.0f);

            // Cria a matriz de rotação em torno do eixo Y
            rotationMatrix = new Matrix4f().rotate(rotationAngle, 0.0f, 1.0f, 0.0f);

            // Define a matriz de modelo com a rotação
            Matrix4f modelMatrix = new Matrix4f();
            modelMatrix.identity();
            modelMatrix.mul(rotationMatrix);

            // Define a localização da matriz de modelo no shader
            int modelMatrixLocation = glGetUniformLocation(shaderProgram, "modelMatrix");
            glUseProgram(shaderProgram);
            glUniformMatrix4fv(modelMatrixLocation, false, modelMatrix.get(new float[16]));

            // Desenha o cubo
//            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES, 0, 36);

            // Troca buffers e lê eventos
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
