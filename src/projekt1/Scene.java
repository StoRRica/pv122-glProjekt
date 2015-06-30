package projekt1;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import static javax.media.opengl.GL2.*;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_EMISSION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT2;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPOT_CUTOFF;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPOT_DIRECTION;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author Adam Jurcik <xjurc@fi.muni.cz>
 */
public class Scene implements GLEventListener {

    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private FPSAnimator animator;

    private int mode = 2;
    private int polygonModes[] = {GL_POINT, GL_LINE, GL_FILL};

    private float time = -0.1f;
    private float y = 0f;

    private int floorId;
    private int tableId;
    private int binId;
    private int bulbId;
    private int vaseId;

    private float cameraX = 0.0f;
    private float cameraY = 0.0f;
    private float scroll = 1.0f;
    private float cameraW = 0.f;

    private static final float[] BLACK = {0.0f, 0.0f, 0.0f, 1.0f};
    private static final float[] WHITE = {1.0f, 1.0f, 1.0f, 1.0f};

    private Texture wood;
    private Texture rocks;
    private Texture glass;

    private boolean ligth0 = false;
    private boolean ligth1 = true;
    private boolean ligth2 = false;
    private boolean ligth3 = true;
    private boolean ligth4 = true;

    public Scene(FPSAnimator animator) {
        this.animator = animator;
    }

    public void togglePolygonMode() {
        mode = (++mode) % 3;
    }

    @Override
    public void init(GLAutoDrawable glad) {
        // Get GL2 interface
        GL2 gl = glad.getGL().getGL2();

        // Enable depth testing
        gl.glEnable(GL_DEPTH_TEST);

        // Enable lighting
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);
        gl.glEnable(GL_LIGHT1);
        gl.glEnable(GL_LIGHT2);
        gl.glEnable(GL_LIGHT3);
        gl.glEnable(GL_LIGHT4);
        gl.glEnable(GL_TEXTURE_2D);

        // Use color from vertex as material component (diffuse, ...)
        //gl.glEnable(GL_COLOR_MATERIAL);
        // Create floor
        createFloor(gl, 10.0f, 250, 4);
        tableId = gl.glGenLists(1);
        //System.out.println("tableId: "+tableId+" floorId: "+floorId);
        ObjLoader objLoader = new ObjLoader("/projekt1/res/objects/table.obj");
        //System.out.println("nacitaj z: " + objLoader.getPath());
        objLoader.load();

        createObj(gl, objLoader, tableId);

        objLoader = new ObjLoader("/projekt1/res/objects/bin.obj");
        objLoader.load();
        binId = gl.glGenLists(1);
        createObj(gl, objLoader, binId);

        bulbId = gl.glGenLists(1);
        objLoader = new ObjLoader("/projekt1/res/objects/bulb.obj");
        objLoader.load();
        createObj(gl, objLoader, bulbId);

        vaseId = gl.glGenLists(1);
        objLoader = new ObjLoader("/projekt1/res/objects/vase2.obj");
        objLoader.load();
        createObj(gl, objLoader, vaseId);
        try {
            wood = loadTexture(gl, "/res/textures/wood.jpg", TextureIO.JPG);
            rocks = loadTexture(gl, "/res/textures/rocks.jpg", TextureIO.JPG);
            glass = loadTexture(gl, "/res/textures/glass.jpg", TextureIO.JPG);
        } catch (IOException e) {
            System.err.println("Resource loading failed. " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
    }

    @Override
    public void display(GLAutoDrawable glad) {
        // Get GL2 interface
        GL2 gl = glad.getGL().getGL2();
        drawAxes(gl, 5);
        if (ligth0) {
            gl.glEnable(GL_LIGHT0);
        } else {
            gl.glDisable(GL_LIGHT0);
        }
        if (ligth1) {
            gl.glEnable(GL_LIGHT1);
        } else {
            gl.glDisable(GL_LIGHT1);
        }
        if (ligth2) {
            gl.glEnable(GL_LIGHT2);
        } else {
            gl.glDisable(GL_LIGHT2);
        }
        if (ligth3) {
            gl.glEnable(GL_LIGHT3);
        } else {
            gl.glDisable(GL_LIGHT3);
        }
        if (ligth4) {
            gl.glEnable(GL_LIGHT4);
        } else {
            gl.glDisable(GL_LIGHT4);
        }
        // Clear buffers
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl.glPolygonMode(GL_FRONT_AND_BACK, polygonModes[mode]);

        if (animator.isAnimating()) {
            time += 0.1f;
            y = 0.5f * (float) (Math.sin(time) + 1);
        }

        // Set look at matrix
        gl.glLoadIdentity();
        glu.gluLookAt(0, 5 * scroll, 5 * scroll,
                0, 0, 0,
                0, 1, 0);

        // Flight around the floor
        gl.glRotatef(cameraX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(cameraY, 0.0f, 1.0f, 0.0f);

        float[] mdl = new float[16];
        float[] camera_org = new float[3];
        gl.glGetFloatv(GL_MODELVIEW_MATRIX, mdl, 0);
        camera_org[0] = -(mdl[0] * mdl[12] + mdl[1] * mdl[13] + mdl[2] * mdl[14]);
        camera_org[1] = -(mdl[4] * mdl[12] + mdl[5] * mdl[13] + mdl[6] * mdl[14]);
        camera_org[2] = -(mdl[8] * mdl[12] + mdl[9] * mdl[13] + mdl[10] * mdl[14]);

        float lightColor[] = {0.5f, 0.0f, 0.0f, 1.0f};
        gl.glPushMatrix();
        gl.glShadeModel(GL_SMOOTH);
        gl.glLoadIdentity();
        float lightPos[] = {camera_org[0], camera_org[2], camera_org[1], 1.0f};
        //System.out.println("light pos:" + lightPos[0] + " , " + lightPos[1] + " , " + lightPos[2]);
        float ambient[] = {0.2f, 0.2f, 0.2f, 1.0f};
        float specular[] = {0.2f, 0.2f, 0.2f, 1.0f};
        gl.glShadeModel(GL_SMOOTH);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, lightColor, 0);

        gl.glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, 360.0f);
        gl.glLightf(GL_LIGHT0, GL_SPOT_EXPONENT, 2.0f);

        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, specular, 0);
        gl.glPopMatrix();

        /*Light 1*/
        gl.glPushMatrix();
        gl.glLightf(GL_LIGHT1, GL_SPOT_CUTOFF, 45.f);
        float direction[] = {0.0f, -1.0f, 0.0f};
        gl.glLightfv(GL_LIGHT1, GL_SPOT_DIRECTION, direction, 0);
        gl.glLightf(GL_LIGHT1, GL_SPOT_EXPONENT, 0.0f);
        float diffuse[] = {1, 0, 0, 1};
        float specular1[] = {1, 1, 1, 1};
        gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL_LIGHT1, GL_SPECULAR, specular, 0);
        if (animator.isAnimating()) {
            lightPos[0] = y - 0.5f;
        } else {
            lightPos[0] = 0.f;
        }
        lightPos[1] = 3f;
        lightPos[2] = 0f;
        lightPos[3] = 1f;
        float ligtC1[] = {0.f, 1.f, 0.f, 1f};
        //System.out.println("light pos:" + lightPos[0] + " , " + lightPos[1] + " , " + lightPos[2]);
        gl.glLightfv(GL_LIGHT1, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, ligtC1, 0);
        gl.glPopMatrix();

        /*light2*/
        gl.glPushMatrix();
        lightPos[0] = 0.0f;
        lightPos[1] = 10.0f;
        lightPos[2] = 0.0f;
        lightPos[3] = 0.0f;
        direction[0] = 0;
        direction[1] = 0;
        direction[2] = 0;
        float noAmbient[] = {0.0f, 0.0f, 0.2f, 1.0f};

        float lightC2[] = {0.f, 0.f, 1.f, 1.f};
        gl.glLightfv(GL_LIGHT2, GL_AMBIENT, noAmbient, 0);
        gl.glLightfv(GL_LIGHT2, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT2, GL_DIFFUSE, lightC2, 0);
        gl.glLightfv(GL_LIGHT2, GL_SPOT_DIRECTION, direction, 0);
        gl.glLightf(GL_LIGHT2, GL_SPOT_CUTOFF, 360.0f);

        gl.glPopMatrix();

        /*light3
         */
        gl.glPushMatrix();
        direction[1] = -1.0f;
        gl.glLightf(GL_LIGHT3, GL_SPOT_CUTOFF, 45.f);
        //float direction[] = {0.0f, -1.0f, 0.0f};
        gl.glLightfv(GL_LIGHT3, GL_SPOT_DIRECTION, direction, 0);
        gl.glLightf(GL_LIGHT3, GL_SPOT_EXPONENT, 0.0f);
        // float diffuse[] = {1, 0, 0, 1};
        float specular2[] = {1, 1, 1, 1};
        gl.glLightfv(GL_LIGHT3, GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL_LIGHT3, GL_SPECULAR, specular, 0);
        if (animator.isAnimating()) {
            lightPos[2] = y - 0.5f;
        } else {
            lightPos[2] = 0f;
        }
        lightPos[1] = 3f;
        lightPos[0] = 0.9f;
        lightPos[3] = 1f;
        float ligtC3[] = {1.f, 0.f, 0.f, 1.f};
        //System.out.println("light pos:" + lightPos[0] + " , " + lightPos[1] + " , " + lightPos[2]);
        gl.glLightfv(GL_LIGHT3, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT3, GL_DIFFUSE, ligtC3, 0);
        gl.glPopMatrix();

        /*light4*/
        gl.glPushMatrix();
        direction[1] = -1.0f;
        gl.glLightf(GL_LIGHT4, GL_SPOT_CUTOFF, 45.f);
        //float direction[] = {0.0f, -1.0f, 0.0f};
        gl.glLightfv(GL_LIGHT4, GL_SPOT_DIRECTION, direction, 0);
        gl.glLightf(GL_LIGHT4, GL_SPOT_EXPONENT, 0.0f);
        // float diffuse[] = {1, 0, 0, 1};

        gl.glLightfv(GL_LIGHT4, GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL_LIGHT4, GL_SPECULAR, specular, 0);
        if (animator.isAnimating()) {
            lightPos[2] = 0.5f - y;
        } else {
            lightPos[2] = 0f;
        }
        lightPos[0] = -0.9f;
        lightPos[1] = 3f;

        lightPos[3] = 1f;
        float ligtC4[] = {0.f, 0.f, 1.f, 1.f};
        //System.out.println("light pos:" + lightPos[0] + " , " + lightPos[1] + " , " + lightPos[2]);
        gl.glLightfv(GL_LIGHT4, GL_POSITION, lightPos, 0);
        gl.glLightfv(GL_LIGHT4, GL_DIFFUSE, ligtC4, 0);
        gl.glPopMatrix();

        //drawAxes(gl, 2);
        float floorColor[] = {0.6f, 0.6f, 0.6f, 1.0f};
        //float tableColor[] = {0.0f, 0.0f, 1.0f, 1.0f};
        float binColor[] = {0.3f, 0.3f, 0.3f, 1f};
        //float vaseColor[] = {0f, 1f, 1f, 1f};
        float bulbColor[] = {1f, 1f, 1f, 1f};
        //gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, floorColor, 0);
        // Use color from vertices as the diffuse component of the material
        //gl.glColorMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE);

        // Turn off emission for the floor material
        gl.glPushMatrix();
        rocks.bind(gl);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, BLACK, 0);
        // Turn on specular highlight for the floor
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, WHITE, 0);
        gl.glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 2.0f);
        gl.glCallList(floorId);
        gl.glPopMatrix();

        gl.glPushMatrix();
        wood.bind(gl);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        //gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, tableColor, 0);
        gl.glCallList(tableId);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.0f, 2f);
        gl.glEnable(GL_NORMALIZE);
        //gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, binColor, 0);
        if (animator.isAnimating()) {
            gl.glTranslatef(0.5f - y, 0.0f, 0.f);
            gl.glScalef(1.f - y * 0.3f, 1 - y * 0.3f, 1 - y * 0.3f);
            gl.glRotatef(time * 3, 0, 1, 0);
        }
        glass.bind(gl);
        gl.glCallList(binId);
        gl.glPopMatrix();
        /* */
        glass.bind(gl);
        //gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, bulbColor, 0);
        gl.glPushMatrix();
        if (ligth1) {
            gl.glDisable(GL_LIGHTING);
        }
        gl.glTranslatef(0f, 3f, 0f);
        if (animator.isAnimating()) {
            gl.glTranslatef(y - 0.5f, 0.0f, 0.f);
        }
        gl.glCallList(bulbId);
        gl.glEnable(GL_LIGHTING);
        gl.glPopMatrix();

        gl.glPushMatrix();
        if (ligth3) {
            gl.glDisable(GL_LIGHTING);
        }
        gl.glTranslatef(0.9f, 3f, 0f);
        if (animator.isAnimating()) {
            gl.glTranslatef(0f, 0.f, y - 0.5f);
        }
        gl.glCallList(bulbId);
        gl.glEnable(GL_LIGHTING);
        gl.glPopMatrix();

        gl.glPushMatrix();
        if (ligth4) {
            gl.glDisable(GL_LIGHTING);
        }
        gl.glTranslatef(-0.9f, 3f, 0f);
        if (animator.isAnimating()) {
            gl.glTranslatef(0f, 0.0f, 0.5f - y);
        }
        gl.glCallList(bulbId);
        gl.glEnable(GL_LIGHTING);
        gl.glPopMatrix();

        //gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, vaseColor, 0);
        gl.glPushMatrix();
        gl.glTranslatef(1.f, 1.79f, 0f);
        gl.glCallList(vaseId);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 1.79f, 0f);
        gl.glCallList(vaseId);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(0.f, 1.79f, 0f);
        gl.glCallList(vaseId);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 1.79f, 0f);
        gl.glCallList(vaseId);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(-1f, 1.79f, 0f);
        gl.glCallList(vaseId);

        // INFO: This is needed due to bug in JOGL
        gl.glPopMatrix();
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public void resetTime() {
        time = -0.1f;
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        // Get GL2 interface
        GL2 gl = glad.getGL().getGL2();

        // Use projection matrix
        gl.glMatrixMode(GL_PROJECTION);

        // Set up perspective projection matrix
        gl.glLoadIdentity();
        glu.gluPerspective(60, ((double) width) / height, 1.0, 1000.0);

        // Part of the image where the scene will be renderer, (0, 0) is bottom left
        gl.glViewport(0, 0, width, height);

        // Use model view matrix
        gl.glMatrixMode(GL_MODELVIEW);
    }

    public void increasePitch() {
        cameraX++;
    }

    public void decreasePitch() {
        cameraX--;
    }

    public void increaseYaw() {
        cameraY++;
    }

    public void decreaseYaw() {
        cameraY--;
    }

    public void incraseSize() {
        scroll += 0.1f;
        // System.out.println("size: "+scroll);
    }

    public void decreseSize() {
        scroll -= 0.1f;
        // System.out.println("size: "+scroll);
    }

    public void cameraForward() {
        cameraW += 0.1f;
    }

    public void camereBackward() {
        cameraW -= 0.1f;
    }

    private void drawAxes(GL2 gl, float size) {
        // Store OpenGL attributes, e.g., previous state of lighting
        gl.glPushMatrix();
        gl.glPushAttrib(GL_ALL_ATTRIB_BITS);

        gl.glDisable(GL_LIGHTING);
        gl.glLineWidth(3.0f);

        gl.glBegin(GL_LINES);

        // X - red
        gl.glColor3f(1, 0, 0);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(size, 0.0f, 0.0f);
        // Y - green
        gl.glColor3f(0, 1, 0);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.0f, size, 0.0f);
        // Z - blue
        gl.glColor3f(0, 0, 1);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, size);

        gl.glEnd();

        // Restore OpenGL attributes
        gl.glPopAttrib();
        gl.glPopMatrix();
    }

    private void createFloor(GL2 gl, float size, int tiles, int repetition) {
        floorId = gl.glGenLists(1);
        gl.glNewList(floorId, GL_COMPILE);

        float step = size / tiles;
        float xstart = -0.5f * size;
        float zstart = 0.5f * size;
        float texSize = 1 / (float) (tiles);
        gl.glNewList(floorId, GL_COMPILE);
        gl.glNormal3f(0.0f, 1.0f, 0.0f);

        for (int z = 0; z < tiles; z++) {
            gl.glBegin(GL_QUAD_STRIP);
            for (int x = 0; x <= tiles; x++) {
                gl.glTexCoord2f(x * texSize * repetition, repetition * (z + 1) * texSize);
                gl.glVertex3f(xstart + x * step, -0.01f, zstart - (z + 1) * step);
                gl.glTexCoord2f(x * texSize * repetition, z * texSize * repetition);
                gl.glVertex3f(xstart + x * step, -0.01f, zstart - z * step);

            }
            gl.glEnd();
        }

        gl.glEndList();
    }

    private void createObj(GL2 gl, ObjLoader object, int listNum) {
        gl.glNewList(listNum, GL_COMPILE);
        gl.glBegin(GL_TRIANGLES);
        for (int i = 0; i < object.getVertexIndices().size(); i++) {
            for (int j = 0; j < 3; j++) {
                float tex1 = object.getTextureCoordinates().get(object.getTextureIndices().get(i)[j])[0];
                float tex2 = object.getTextureCoordinates().get(object.getTextureIndices().get(i)[j])[1];
              
                gl.glTexCoord2f(tex2, tex1);
                float div = 40f;
                float n1 = object.getNormals().get(object.getNormalIndices().get(i)[j])[0];
                float n2 = object.getNormals().get(object.getNormalIndices().get(i)[j])[1];
                float n3 = object.getNormals().get(object.getNormalIndices().get(i)[j])[2];
                //System.out.println("n1:" +n1+" n2: "+n2+" n3: "+n3);
                gl.glNormal3f(n1, n2, n3);

                float v1 = object.getVertices().get(object.getVertexIndices().get(i)[j])[0] / div;
                float v2 = object.getVertices().get(object.getVertexIndices().get(i)[j])[1] / div;
                float v3 = object.getVertices().get(object.getVertexIndices().get(i)[j])[2] / div;
                //System.out.println("v1:" +v1+" v2: "+v2+" v3: "+v3);
                gl.glVertex3f(v1, v2, v3);

              
            }
        }
        gl.glEnd();
        gl.glEndList();

    }

    private Texture loadTexture(GL2 gl, String filename, String suffix) throws IOException {
        try (InputStream is = Scene.class.getResourceAsStream(filename)) {
            Texture tex = TextureIO.newTexture(is, true, suffix);
            // Set texture filters
            tex.setTexParameteri(gl, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            tex.setTexParameteri(gl, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            // Set texture coordinates wrap mode
            tex.setTexParameteri(gl, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            tex.setTexParameteri(gl, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            // Unbind texture
            gl.glBindTexture(GL_TEXTURE_2D, 0);
            return tex;
        }
    }

    public void toggleLigth0() {
        ligth0 = !ligth0;
    }

    public void toggleLigth1() {
        ligth1 = !ligth1;
    }

    public void toggleLigth2() {
        ligth2 = !ligth2;
    }

    public void toggleLigth3() {
        ligth3 = !ligth3;
    }

    public void toggleLigth4() {
        ligth4 = !ligth4;
    }

}
