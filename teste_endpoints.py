import pytest
import requests
from dataclasses import dataclass
from urllib.parse import urljoin
import json
import re
import logging

BASE_URL = "http://localhost:8080/"


def url(rota: str) -> str:
    return urljoin(BASE_URL, rota)


@dataclass
class Aluno:
    nome: str
    _email: str

    @property
    def email(self):
        match = re.findall(r'.+@.+\..+', self._email)
        return match[0] if match else None


@dataclass
class Resposta:
    codigo: int
    cont: bytes


@dataclass
class MapperGetAluno:
    resposta: Resposta

    def respostaToAluno(self) -> list[Aluno]:
        e = json.loads(self.resposta.cont)
        return [Aluno(f['nome'], f['email']) for f in e]


def resposta(sub_rota: str, headers: dict = {}) -> Resposta:
    s = requests.get(url=url(sub_rota), headers=headers)
    return Resposta(s.status_code, s._content)


# --- autenticação ---

@pytest.fixture(scope="session")
def auth_headers():
    r = requests.post(url("auth/login"), json={"login": "admin", "senha": "admin"})
    assert r.status_code == 200, f"Falha ao autenticar: {r.status_code} {r.text}"
    token = r.json()["token"]
    return {"Authorization": f"Bearer {token}"}


# --- testes segurança ---

def test_rota_protegida_sem_token():
    r = requests.get(url("aluno"))
    assert r.status_code == 401

def test_rota_usuario_sem_token():
    r = requests.get(url("usuario"))
    assert r.status_code == 401

def test_rota_curso_sem_token():
    r = requests.get(url("curso"))
    assert r.status_code == 401

def test_rota_matricula_sem_token():
    r = requests.get(url("matricula"))
    assert r.status_code == 401

def test_cadastrar_usuario_sem_token():
    payload = {"login": "semtoken@teste.com", "nome": "Sem Token", "senha": "Senha@123"}
    r = requests.post(url("usuario"), json=payload)
    assert r.status_code == 201
    requests.delete(url(f"usuario/{r.json()['id']}"), headers={"Authorization": "Bearer " + requests.post(url("auth/login"), json={"login": "admin", "senha": "admin"}).json()["token"]})


# --- testes aluno ---

@pytest.fixture
def aluno_criado(auth_headers):
    r = requests.post(url("aluno"), json={"nome": "Fixture Aluno", "email": "fixture@teste.com"}, headers=auth_headers)
    assert r.status_code == 201, f"Falha ao criar aluno no fixture: {r.status_code}"
    aluno = r.json()
    yield aluno
    requests.delete(url(f"aluno/{aluno['id']}"), headers=auth_headers)


def test_inserir_aluno(auth_headers):
    payload = {"nome": "Claudio Inserido", "email": "claudio.inserir@teste.com"}

    r = requests.post(url("aluno"), json=payload, headers=auth_headers)

    assert r.status_code == 201
    body = r.json()
    assert body["nome"] == payload["nome"]
    assert body["email"] == payload["email"]
    assert body["id"] is not None

    requests.delete(url(f"aluno/{body['id']}"), headers=auth_headers)


def test_alterar_aluno(aluno_criado, auth_headers):
    novo_dados = {"nome": "Claudio Alterado", "email": "fixture@teste.com"}

    r = requests.put(url(f"aluno/{aluno_criado['id']}"), json=novo_dados, headers=auth_headers)

    assert r.status_code == 200
    body = r.json()
    assert body["nome"] == novo_dados["nome"]
    assert body["email"] == novo_dados["email"]


def test_deletar_aluno(aluno_criado, auth_headers):
    r = requests.delete(url(f"aluno/{aluno_criado['id']}"), headers=auth_headers)

    assert r.status_code == 204

    r = requests.get(url(f"aluno/{aluno_criado['id']}"), headers=auth_headers)
    assert r.status_code == 404


# --- casos de erro aluno ---

def test_inserir_aluno_email_duplicado(aluno_criado, auth_headers):
    r = requests.post(url("aluno"), json={"nome": "Outro Nome", "email": "fixture@teste.com"}, headers=auth_headers)

    assert r.status_code == 409
    assert r.json()["error"] == "CONFLICT"


def test_buscar_aluno_inexistente(auth_headers):
    r = requests.get(url("aluno/999999"), headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_alterar_aluno_inexistente(auth_headers):
    r = requests.put(url("aluno/999999"), json={"nome": "Ninguem", "email": "ninguem@teste.com"}, headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_deletar_aluno_inexistente(auth_headers):
    r = requests.delete(url("aluno/999999"), headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


# --- testes curso ---

@pytest.fixture
def curso_criado(auth_headers):
    r = requests.post(url("curso"), json={"nome": "Curso Fixture", "descricao": "Descricao fixture", "cargaHoraria": 40}, headers=auth_headers)
    assert r.status_code == 201, f"Falha ao criar curso no fixture: {r.status_code}"
    curso = r.json()
    yield curso
    requests.delete(url(f"curso/{curso['id']}"), headers=auth_headers)


def test_inserir_curso(auth_headers):
    payload = {"nome": "Curso Inserido", "descricao": "Descricao teste", "cargaHoraria": 60}

    r = requests.post(url("curso"), json=payload, headers=auth_headers)

    assert r.status_code == 201
    body = r.json()
    assert body["nome"] == payload["nome"]
    assert body["descricao"] == payload["descricao"]
    assert body["cargaHoraria"] == payload["cargaHoraria"]
    assert body["id"] is not None

    requests.delete(url(f"curso/{body['id']}"), headers=auth_headers)


def test_alterar_curso(curso_criado, auth_headers):
    novo_dados = {"nome": "Curso Alterado", "descricao": "Nova descricao", "cargaHoraria": 80}

    r = requests.put(url(f"curso/{curso_criado['id']}"), json=novo_dados, headers=auth_headers)

    assert r.status_code == 200
    body = r.json()
    assert body["nome"] == novo_dados["nome"]
    assert body["descricao"] == novo_dados["descricao"]
    assert body["cargaHoraria"] == novo_dados["cargaHoraria"]


def test_deletar_curso(curso_criado, auth_headers):
    r = requests.delete(url(f"curso/{curso_criado['id']}"), headers=auth_headers)

    assert r.status_code == 204

    r = requests.get(url(f"curso/{curso_criado['id']}"), headers=auth_headers)
    assert r.status_code == 404


def test_inserir_curso_nome_duplicado(curso_criado, auth_headers):
    r = requests.post(url("curso"), json={"nome": "Curso Fixture", "descricao": "Outra descricao", "cargaHoraria": 20}, headers=auth_headers)

    assert r.status_code == 409
    assert r.json()["error"] == "CONFLICT"


def test_buscar_curso_inexistente(auth_headers):
    r = requests.get(url("curso/999999"), headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_alterar_curso_inexistente(auth_headers):
    r = requests.put(url("curso/999999"), json={"nome": "Ninguem", "descricao": "x", "cargaHoraria": 1}, headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_deletar_curso_inexistente(auth_headers):
    r = requests.delete(url("curso/999999"), headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


# --- testes usuario ---

@pytest.fixture
def usuario_criado(auth_headers):
    r = requests.post(url("usuario"), json={"login": "fixture.usuario@teste.com", "nome": "Fixture Usuario", "senha": "Senha@123"}, headers=auth_headers)
    assert r.status_code == 201, f"Falha ao criar usuario no fixture: {r.status_code}"
    usuario = r.json()
    yield usuario
    requests.delete(url(f"usuario/{usuario['id']}"), headers=auth_headers)


def test_inserir_usuario(auth_headers):
    payload = {"login": "usuario.inserido@teste.com", "nome": "Usuario Inserido", "senha": "Senha@123"}

    r = requests.post(url("usuario"), json=payload, headers=auth_headers)

    assert r.status_code == 201
    body = r.json()
    assert body["login"] == payload["login"]
    assert body["id"] is not None

    requests.delete(url(f"usuario/{body['id']}"), headers=auth_headers)



def test_inserir_usuario_login_duplicado(usuario_criado, auth_headers):
    r = requests.post(url("usuario"), json={"login": "fixture.usuario@teste.com", "nome": "Outro Nome", "senha": "Outra@123"}, headers=auth_headers)

    assert r.status_code == 409
    assert r.json()["error"] == "CONFLICT"


def test_buscar_usuario_inexistente(auth_headers):
    r = requests.get(url("usuario/999999"), headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


# --- testes de validação usuario ---

def test_usuario_sem_nome(auth_headers):
    r = requests.post(url("usuario"), json={"login": "semNome@teste.com", "senha": "Senha@123"}, headers=auth_headers)
    assert r.status_code == 400
    assert r.json()["error"] == "BAD_REQUEST"
    assert "nome" in r.json()["message"].lower()


def test_usuario_login_invalido(auth_headers):
    r = requests.post(url("usuario"), json={"login": "nao-e-email", "nome": "Teste", "senha": "Senha@123"}, headers=auth_headers)
    assert r.status_code == 400
    assert r.json()["error"] == "BAD_REQUEST"
    assert "login" in r.json()["message"]
    assert "email" in r.json()["message"].lower()


def test_usuario_senha_sem_maiuscula(auth_headers):
    r = requests.post(url("usuario"), json={"login": "test@email.com", "nome": "Teste", "senha": "senha@123"}, headers=auth_headers)
    assert r.status_code == 400
    assert r.json()["error"] == "BAD_REQUEST"
    assert "senha" in r.json()["message"].lower()


def test_usuario_senha_sem_numero(auth_headers):
    r = requests.post(url("usuario"), json={"login": "test2@email.com", "nome": "Teste", "senha": "Senha@abc"}, headers=auth_headers)
    assert r.status_code == 400
    assert r.json()["error"] == "BAD_REQUEST"
    assert "senha" in r.json()["message"].lower()


def test_usuario_senha_sem_simbolo(auth_headers):
    r = requests.post(url("usuario"), json={"login": "test3@email.com", "nome": "Teste", "senha": "Senha1234"}, headers=auth_headers)
    assert r.status_code == 400
    assert r.json()["error"] == "BAD_REQUEST"
    assert "senha" in r.json()["message"].lower()


def test_usuario_senha_curta(auth_headers):
    r = requests.post(url("usuario"), json={"login": "test4@email.com", "nome": "Teste", "senha": "Se@1"}, headers=auth_headers)
    assert r.status_code == 400
    assert r.json()["error"] == "BAD_REQUEST"
    assert "senha" in r.json()["message"].lower()


def test_usuario_valido(auth_headers):
    payload = {"login": "valido@email.com", "nome": "Usuario Valido", "senha": "Senha@123"}
    r = requests.post(url("usuario"), json=payload, headers=auth_headers)
    assert r.status_code == 201
    requests.delete(url(f"usuario/{r.json()['id']}"), headers=auth_headers)


# --- testes de persistência (não limpam os dados) ---

def test_persistencia_aluno(auth_headers):
    payload = {"nome": "Aluno Persistente", "email": "persistente.aluno@teste.com"}
    r = requests.post(url("aluno"), json=payload, headers=auth_headers)
    assert r.status_code == 201
    id = r.json()["id"]

    r = requests.get(url(f"aluno/{id}"), headers=auth_headers)
    assert r.status_code == 200
    assert r.json()["nome"] == payload["nome"]
    assert r.json()["email"] == payload["email"]


def test_persistencia_curso(auth_headers):
    payload = {"nome": "Curso Persistente", "descricao": "Descricao persistente", "cargaHoraria": 30}
    r = requests.post(url("curso"), json=payload, headers=auth_headers)
    assert r.status_code == 201
    id = r.json()["id"]

    r = requests.get(url(f"curso/{id}"), headers=auth_headers)
    assert r.status_code == 200
    assert r.json()["nome"] == payload["nome"]
    assert r.json()["descricao"] == payload["descricao"]
    assert r.json()["cargaHoraria"] == payload["cargaHoraria"]


def test_persistencia_usuario(auth_headers):
    payload = {"login": "usuario.persistente@teste.com", "nome": "Usuario Persistente", "senha": "Senha@123"}
    r = requests.post(url("usuario"), json=payload, headers=auth_headers)
    assert r.status_code == 201
    id = r.json()["id"]

    r = requests.get(url(f"usuario/{id}"), headers=auth_headers)
    assert r.status_code == 200
    assert r.json()["login"] == payload["login"]


def test_listar_todos_alunos(auth_headers):
    r = requests.get(url("aluno"), headers=auth_headers)
    assert r.status_code == 200
    assert isinstance(r.json(), list)


def test_listar_todos_cursos(auth_headers):
    r = requests.get(url("curso"), headers=auth_headers)
    assert r.status_code == 200
    assert isinstance(r.json(), list)


def test_listar_todos_usuarios(auth_headers):
    r = requests.get(url("usuario"), headers=auth_headers)
    assert r.status_code == 200
    assert isinstance(r.json(), list)
    logins = [u["login"] for u in r.json()]
    assert "admin" in logins


# --- testes matricula ---

@pytest.fixture
def matricula_criada(aluno_criado, curso_criado, auth_headers):
    r = requests.post(url("matricula"), json={"alunoId": aluno_criado["id"], "cursoId": curso_criado["id"]}, headers=auth_headers)
    assert r.status_code == 201, f"Falha ao criar matrícula no fixture: {r.status_code}"
    matricula = r.json()
    yield matricula
    requests.delete(url(f"matricula/{matricula['id']}"), headers=auth_headers)


def test_inserir_matricula(aluno_criado, curso_criado, auth_headers):
    r = requests.post(url("matricula"), json={"alunoId": aluno_criado["id"], "cursoId": curso_criado["id"]}, headers=auth_headers)

    assert r.status_code == 201
    body = r.json()
    assert body["alunoId"] == aluno_criado["id"]
    assert body["cursoId"] == curso_criado["id"]
    assert body["dataMatricula"] is not None

    requests.delete(url(f"matricula/{body['id']}"), headers=auth_headers)


def test_buscar_matricula_por_id(matricula_criada, auth_headers):
    r = requests.get(url(f"matricula/{matricula_criada['id']}"), headers=auth_headers)

    assert r.status_code == 200
    assert r.json()["id"] == matricula_criada["id"]


def test_deletar_matricula(matricula_criada, auth_headers):
    r = requests.delete(url(f"matricula/{matricula_criada['id']}"), headers=auth_headers)

    assert r.status_code == 204

    r = requests.get(url(f"matricula/{matricula_criada['id']}"), headers=auth_headers)
    assert r.status_code == 404


def test_matricula_duplicada(matricula_criada, aluno_criado, curso_criado, auth_headers):
    r = requests.post(url("matricula"), json={"alunoId": aluno_criado["id"], "cursoId": curso_criado["id"]}, headers=auth_headers)

    assert r.status_code == 409
    assert r.json()["error"] == "CONFLICT"


def test_matricula_aluno_inexistente(curso_criado, auth_headers):
    r = requests.post(url("matricula"), json={"alunoId": 999999, "cursoId": curso_criado["id"]}, headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_matricula_curso_inexistente(aluno_criado, auth_headers):
    r = requests.post(url("matricula"), json={"alunoId": aluno_criado["id"], "cursoId": 999999}, headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_buscar_matricula_inexistente(auth_headers):
    r = requests.get(url("matricula/999999"), headers=auth_headers)

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_listar_matriculas(auth_headers):
    r = requests.get(url("matricula"), headers=auth_headers)

    assert r.status_code == 200
    assert isinstance(r.json(), list)


def test_persistencia_matricula(auth_headers):
    aluno = requests.post(url("aluno"), json={"nome": "Aluno Persistente Matricula", "email": "persistente.matricula@teste.com"}, headers=auth_headers).json()
    curso = requests.post(url("curso"), json={"nome": "Curso Persistente Matricula", "descricao": "Desc", "cargaHoraria": 10}, headers=auth_headers).json()

    r = requests.post(url("matricula"), json={"alunoId": aluno["id"], "cursoId": curso["id"]}, headers=auth_headers)
    assert r.status_code == 201
    matricula_id = r.json()["id"]

    r = requests.get(url(f"matricula/{matricula_id}"), headers=auth_headers)
    assert r.status_code == 200
    assert r.json()["alunoId"] == aluno["id"]
    assert r.json()["cursoId"] == curso["id"]


if __name__ == '__main__':
    logging.basicConfig(level=logging.INFO)

    # autentica
    r = requests.post(url("auth/login"), json={"login": "admin", "senha": "admin"})
    if r.status_code != 200:
        logging.error("Falha ao autenticar: %d %s", r.status_code, r.text)
        exit(1)

    headers = {"Authorization": f"Bearer {r.json()['token']}"}

    for rota in ['aluno', 'curso', 'usuario']:
        _r = resposta(rota, headers)
        logging.info(
            '%s: status code: %d : %s',
            *(('sucesso na requisição', _r.codigo, _r.cont) if 199 < _r.codigo < 299 else ('falha na requisição', _r.codigo, _r.cont)),
        )
