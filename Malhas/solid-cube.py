import numpy as np
from glumpy import app, gl, glm, gloo

vertex = """
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

attribute vec3 position;

void main()
{
    gl_Position = projection * view * model * vec4(position,1.0);
}
"""

fragment = """
void main()
{
    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
"""

window = app.Window()


@window.event
def on_draw(dt):
    window.clear()

    model = np.eye(4, dtype=np.float32)
    cube['model'] = model

    cube.draw(gl.GL_LINE_LOOP, I)


@window.event
def on_resize(width, height):
    cube['projection'] = glm.perspective(45.0, width / float(height), 2.0, 100.0)


@window.event
def on_init():
    gl.glEnable(gl.GL_DEPTH_TEST)


V = np.zeros(8, [("position", np.float32, 3)])
V["position"] = [[1, 1, 1], [-1, 1, 1], [-1, -1, 1], [1, -1, 1],
                 [1, -1, -1], [1, 1, -1], [-1, 1, -1], [-1, -1, -1]]
V = V.view(gloo.VertexBuffer)

I = np.array([0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 0, 5, 6, 0, 6, 1,
              1, 6, 7, 1, 7, 2, 7, 4, 3, 7, 3, 2, 4, 7, 6, 4, 6, 5], dtype=np.uint32)
I = I.view(gloo.IndexBuffer)

cube = gloo.Program(vertex, fragment)
cube.bind(V)
cube['view'] = glm.translation(0, 0, -5)

app.run()
