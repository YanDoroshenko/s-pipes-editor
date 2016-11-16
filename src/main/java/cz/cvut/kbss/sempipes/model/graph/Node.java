package cz.cvut.kbss.sempipes.model.graph;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.sempipes.model.Vocabulary;

import java.net.URI;
import java.util.Set;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 10.11.16.
 */
@OWLClass(iri = Vocabulary.s_c_node)
public class Node {

    @Id(generated = true)
    private URI uri;
    @OWLDataProperty(iri = Vocabulary.s_p_label)
    private String label;
    @OWLDataProperty(iri = Vocabulary.s_p_has_x_coordinate)
    private Double x;
    @OWLDataProperty(iri = Vocabulary.s_p_has_y_coordinate)
    private Double y;
    @Types
    private Set<String> nodeTypes;
    @OWLDataProperty(iri = Vocabulary.s_p_has_input_parameter)
    private Set<String> inParameters;
    @OWLDataProperty(iri = Vocabulary.s_p_has_output_parameter)
    private Set<String> outParameters;

    public Node() {
    }

    public Node(URI uri, String label, double x, double y, Set<String> nodeTypes, Set<String> inParameters, Set<String> outParameters) {
        this.uri = uri;
        this.label = label;
        this.x = x;
        this.y = y;
        this.nodeTypes = nodeTypes;
        this.inParameters = inParameters;
        this.outParameters = outParameters;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Set<String> getNodeTypes() {
        return nodeTypes;
    }

    public void setNodeTypes(Set<String> nodeTypes) {
        this.nodeTypes = nodeTypes;
    }

    public Set<String> getInParameters() {
        return inParameters;
    }

    public void setInParameters(Set<String> inParameters) {
        this.inParameters = inParameters;
    }

    public Set<String> getOutParameters() {
        return outParameters;
    }

    public void setOutParameters(Set<String> outParameters) {
        this.outParameters = outParameters;
    }

    @Override
    public String toString() {
        return "Node{" +
                "uri=" + uri +
                ", label='" + label + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", nodeTypes=" + nodeTypes +
                ", inParameters=" + inParameters +
                ", outParameters=" + outParameters +
                '}';
    }
}