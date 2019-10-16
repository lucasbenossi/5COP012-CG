from typing import NamedTuple, Tuple, Dict

Edge = NamedTuple('Edge', [('vertices', Tuple[str, str]), ('faces', Tuple[str, str])])

Vertex = NamedTuple('Vertex', [('x', float),
                               ('y', float),
                               ('z', float)])


def load_input() -> Dict[str, Edge]:
    with open('input.csv') as fp:
        lines = fp.readlines()
    edges = dict()
    for line in lines:
        vec = line.strip().split(',')
        edges[vec[0]] = Edge((vec[1], vec[2]), (vec[3], vec[4]))
    return edges


def load_coords() -> Dict[str, Vertex]:
    with open('coords.csv') as fp:
        lines = fp.readlines()
    vertices = dict()
    for line in lines:
        vec = line.strip().split(',')
        vertices[vec[0]] = Vertex(float(vec[1]), float(vec[2]), float(vec[3]))
    return vertices


# noinspection PyShadowingBuiltins
def main() -> None:
    input = load_input()

    faces_dict = dict()
    with open('face.csv', 'w') as fp:
        faces_set = {face
                     for i in input.values()
                     for face in i.faces}
        for face in sorted(faces_set):
            face_edges = []
            for k, v in input.items():
                if face in v.faces:
                    face_edges.append(k)
            if len(face_edges) != 3:
                raise Exception('len(face_edges) inválido')
            print(','.join([face, *sorted(face_edges)]), file=fp)
            faces_dict[face] = set(face_edges)

    with open('edge.csv', 'w') as fp:
        for k, edge in input.items():
            edges_set = {e
                         for face in edge.faces
                         for e in faces_dict[face]}
            print(','.join([k, *edge.vertices, *edge.faces, *(edges_set - {k})]), file=fp)

    with open('vertex.csv', 'w') as fp:
        for k, vertex in load_coords().items():
            edge_set = set()
            for edge_k, edge_v in input.items():
                if k in edge_v.vertices:
                    edge_set.add(edge_k)
            print(','.join([k, str(vertex.x), str(vertex.y), str(vertex.z), *edge_set]), file=fp)


if __name__ == '__main__':
    main()
