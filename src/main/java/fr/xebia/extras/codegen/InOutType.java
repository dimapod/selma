package fr.xebia.extras.codegen;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 20/11/2013
 * Time: 02:04
 * To change this template use File | Settings | File Templates.
 */
public class InOutType {

    private final TypeMirror in;
    private final TypeMirror out;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InOutType inOutType = (InOutType) o;

        if (!in.equals(inOutType.in)) return false;
        if (!out.equals(inOutType.out)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = in.hashCode();
        result = 31 * result + out.hashCode();
        return result;
    }

    public InOutType(TypeMirror in, TypeMirror out) {
        this.in = in;
        this.out = out;
    }

    public boolean areSamePrimitive() {
        return in.getKind().isPrimitive() && in.getKind() == out.getKind();
    }

    public boolean areDeclared() {
        return in.getKind() == TypeKind.DECLARED && out.getKind() == TypeKind.DECLARED;
    }

    public DeclaredType inAsDeclaredType() {
        return (DeclaredType) in;
    }

    public TypeElement inAsTypeElement() {
        return (TypeElement) inAsDeclaredType().asElement();
    }

    public boolean areEnums() {
        return inAsTypeElement().getKind() == ElementKind.ENUM && outAsTypeElement().getKind() == ElementKind.ENUM;
    }

    public boolean inIsArray() {
        return in.getKind() == TypeKind.ARRAY;
    }

    public ArrayType inAsArrayType() {
        return (ArrayType) in;
    }

    public boolean isInArrayComponentPrimitive() {
        return inAsArrayType().getComponentType().getKind().isPrimitive();
    }

    public TypeMirror inArrayComponentType() {
        return inAsArrayType().getComponentType();
    }

    public boolean isInArrayComponentDeclared() {
        return inArrayComponentType().getKind() == TypeKind.DECLARED;
    }

    public TypeMirror in() {
        return in;
    }

    public TypeMirror out() {
        return out;
    }

    public TypeMirror outArrayComponentType() {
        return outAsArrayType().getComponentType();
    }

    private ArrayType outAsArrayType() {
        return (ArrayType) out;
    }

    public boolean isInArrayComponentDeclaredOrArray() {
        return isInArrayComponentDeclared() || inArrayComponentType().getKind() == TypeKind.ARRAY;
    }

    public TypeElement outAsTypeElement() {
        return (TypeElement) ((DeclaredType) out).asElement();
    }

    public boolean differs() {
        return !in.toString().equals(out.toString());
    }

    public DeclaredType outAsDeclaredType() {
        return (DeclaredType) out;
    }

    public boolean inIsPrimitive() {
        return in.getKind().isPrimitive();
    }
}