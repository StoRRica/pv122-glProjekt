#include "ObjLoader.h"

vector<Vector3f> & ObjLoader::GetVertices()
{
	return vertices;
}

vector<Vector3f> & ObjLoader::GetNormals()
{
	return normals;
}

vector<Triangle> & ObjLoader::GetTriangles()
{
	return triangles;
}

bool ObjLoader::Load(const char * filename)
{
	ifstream file(filename);
	if (!file.is_open())
	{
		cout << "Error opening file" << endl;
		return false;
	}

	while (!file.fail())
	{
		string prefix;
		file >> prefix;

		if (prefix == "v")
		{
			Vector3f v;
			file >> v.x >> v.y >> v.z;
			vertices.push_back(v);
		}
		else if (prefix == "vt")
		{
			// TODO read texture coordinates
		}
		else if (prefix == "vn")
		{
			Vector3f vn;
			file >> vn.x >> vn.y >> vn.z;
			normals.push_back(vn);
		}
		else if (prefix == "f")
		{
			Triangle t;
			char slash;
			file >> t.v0 >> slash >> t.t0 >> slash >> t.n0;
			file >> t.v1 >> slash >> t.t1 >> slash >> t.n1;
			file >> t.v2 >> slash >> t.t2 >> slash >> t.n2;
			DecrementIndices(t);
			triangles.push_back(t);
		}
		else
		{
			// Other cases
		}
		file.ignore(1000, '\n');
	}

	file.close();
	return true;
}

void ObjLoader::DecrementIndices(Triangle & triangle)
{
	triangle.v0--; triangle.t0--; triangle.n0--;
	triangle.v1--; triangle.t1--; triangle.n1--;
	triangle.v2--; triangle.t2--; triangle.n2--;
}

void ObjLoader::PrintLog()
{
	cout << "Vertices: " << vertices.size() << endl;
	cout << "Normals: " << normals.size() << endl;
	cout << "Triangles: " << triangles.size() << endl;
}