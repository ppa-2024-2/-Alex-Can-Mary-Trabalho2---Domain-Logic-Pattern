package br.edu.ifrs.riogrande.tads.ppa.ligaa.domain;

import java.util.ArrayList;
import java.util.List;

public class Turma {

    private String codigo; // ppa-2024-2

    private Disciplina disciplina;
    private Professor professor;

    private String semestre; // 2024-2
    private String sala;
    private int vagas;
    private boolean fechada;

    public void turmaEstaAberta() {
        if (this.isFechada()) {
            throw new DomainException("Turma " + this.codigo + " está fechada");
        }
    }

    public void haVagas(Historico historico) {
        int qtdAlunosTurma = this.getMatriculas().size();

        int qtdVagas = this.getVagas();

        if (qtdAlunosTurma >= qtdVagas) { // não
            // turmas anteriores da disciplina
            var turmasAnterioresDaDisciplina = historico.turmasAnteriores(this);

            boolean reprovadoAnteriormente = historico.reprovadoAnteriormente(turmasAnterioresDaDisciplina);

            // se nunca foi reprovado, não pode matricular-se
            if (!reprovadoAnteriormente) {
                throw new DomainException("Não há vagas na turma " + this.getCodigo());
            }
        }
    }

    public void estaMatriculado(Aluno aluno) {
        boolean estaMatriculado = this.getMatriculas()
                .stream()
                .anyMatch(m -> m.getAluno().equals(aluno));
        if (estaMatriculado) {
            throw new DomainException("Aluno " + aluno.getCpf() + " já está matriculado na turma " + this.codigo);
        }
    }

    public Matricula matricularAluno(Aluno aluno, Historico historico) {
        this.turmaEstaAberta();
        this.estaMatriculado(aluno);
        historico.alunoAprovadoEm(this, aluno);
        historico.alunoAproveitouAnteriormente(this, aluno);
        this.haVagas(historico);

        Matricula matricula = new Matricula(aluno);
        this.getMatriculas().add(matricula);

        return matricula;
    }

    public boolean isFechada() {
        return fechada;
    }

    public void setFechada(boolean fechada) {
        this.fechada = fechada;
    }

    private List<Matricula> matriculas = new ArrayList<>();

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    @Override
    public String toString() {
        return "Turma [codigo=" + codigo + ", disciplina=" + disciplina + ", professor=" + professor + ", semestre="
                + semestre + ", sala=" + sala + ", vagas=" + vagas + ", matriculas=" + matriculas + "]";
    }
}