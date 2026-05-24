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


def resposta(sub_rota: str) -> Resposta:
    s = requests.get(url=url(sub_rota))
    return Resposta(s.status_code, s._content)


# --- testes ---

@pytest.fixture
def aluno_criado():
    r = requests.post(url("aluno"), json={"nome": "Fixture Aluno", "email": "fixture@teste.com"})
    assert r.status_code == 201, f"Falha ao criar aluno no fixture: {r.status_code}"
    aluno = r.json()
    yield aluno
    requests.delete(url(f"aluno/{aluno['id']}"))


def test_inserir_aluno():
    payload = {"nome": "Claudio Inserido", "email": "claudio.inserir@teste.com"}

    r = requests.post(url("aluno"), json=payload)

    assert r.status_code == 201
    body = r.json()
    assert body["nome"] == payload["nome"]
    assert body["email"] == payload["email"]
    assert body["id"] is not None

    requests.delete(url(f"aluno/{body['id']}"))


def test_alterar_aluno(aluno_criado):
    novo_dados = {"nome": "Claudio Alterado", "email": "fixture@teste.com"}

    r = requests.put(url(f"aluno/{aluno_criado['id']}"), json=novo_dados)

    assert r.status_code == 200
    body = r.json()
    assert body["nome"] == novo_dados["nome"]
    assert body["email"] == novo_dados["email"]


def test_deletar_aluno(aluno_criado):
    r = requests.delete(url(f"aluno/{aluno_criado['id']}"))

    assert r.status_code == 204

    r = requests.get(url(f"aluno/{aluno_criado['id']}"))
    assert r.status_code == 404


# --- casos de erro ---

def test_inserir_aluno_email_duplicado(aluno_criado):
    r = requests.post(url("aluno"), json={"nome": "Outro Nome", "email": "fixture@teste.com"})

    assert r.status_code == 409
    assert r.json()["error"] == "CONFLICT"


def test_buscar_aluno_inexistente():
    r = requests.get(url("aluno/999999"))

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_alterar_aluno_inexistente():
    r = requests.put(url("aluno/999999"), json={"nome": "Ninguem", "email": "ninguem@teste.com"})

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_deletar_aluno_inexistente():
    r = requests.delete(url("aluno/999999"))

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


# --- testes curso ---

@pytest.fixture
def curso_criado():
    r = requests.post(url("curso"), json={"nome": "Curso Fixture", "descricao": "Descricao fixture", "cargaHoraria": 40})
    assert r.status_code == 201, f"Falha ao criar curso no fixture: {r.status_code}"
    curso = r.json()
    yield curso
    requests.delete(url(f"curso/{curso['id']}"))


def test_inserir_curso():
    payload = {"nome": "Curso Inserido", "descricao": "Descricao teste", "cargaHoraria": 60}

    r = requests.post(url("curso"), json=payload)

    assert r.status_code == 201
    body = r.json()
    assert body["nome"] == payload["nome"]
    assert body["descricao"] == payload["descricao"]
    assert body["cargaHoraria"] == payload["cargaHoraria"]
    assert body["id"] is not None

    requests.delete(url(f"curso/{body['id']}"))


def test_alterar_curso(curso_criado):
    novo_dados = {"nome": "Curso Alterado", "descricao": "Nova descricao", "cargaHoraria": 80}

    r = requests.put(url(f"curso/{curso_criado['id']}"), json=novo_dados)

    assert r.status_code == 200
    body = r.json()
    assert body["nome"] == novo_dados["nome"]
    assert body["descricao"] == novo_dados["descricao"]
    assert body["cargaHoraria"] == novo_dados["cargaHoraria"]


def test_deletar_curso(curso_criado):
    r = requests.delete(url(f"curso/{curso_criado['id']}"))

    assert r.status_code == 204

    r = requests.get(url(f"curso/{curso_criado['id']}"))
    assert r.status_code == 404


def test_inserir_curso_nome_duplicado(curso_criado):
    r = requests.post(url("curso"), json={"nome": "Curso Fixture", "descricao": "Outra descricao", "cargaHoraria": 20})

    assert r.status_code == 409
    assert r.json()["error"] == "CONFLICT"


def test_buscar_curso_inexistente():
    r = requests.get(url("curso/999999"))

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_alterar_curso_inexistente():
    r = requests.put(url("curso/999999"), json={"nome": "Ninguem", "descricao": "x", "cargaHoraria": 1})

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


def test_deletar_curso_inexistente():
    r = requests.delete(url("curso/999999"))

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


# --- testes usuario ---

@pytest.fixture
def usuario_criado():
    r = requests.post(url("usuario"), json={"login": "fixture.usuario", "senha": "senha123"})
    assert r.status_code == 201, f"Falha ao criar usuario no fixture: {r.status_code}"
    usuario = r.json()
    yield usuario
    requests.delete(url(f"usuario/{usuario['id']}"))


def test_inserir_usuario():
    payload = {"login": "usuario.inserido", "senha": "senha123"}

    r = requests.post(url("usuario"), json=payload)

    assert r.status_code == 201
    body = r.json()
    assert body["login"] == payload["login"]
    assert body["id"] is not None


def test_inserir_usuario_login_duplicado(usuario_criado):
    r = requests.post(url("usuario"), json={"login": "fixture.usuario", "senha": "outrasenha"})

    assert r.status_code == 409
    assert r.json()["error"] == "CONFLICT"


def test_buscar_usuario_inexistente():
    r = requests.get(url("usuario/999999"))

    assert r.status_code == 404
    assert r.json()["error"] == "NOT_FOUND"


if __name__ == '__main__':
    logging.basicConfig(level=logging.INFO)
    _r = resposta('aluno')
    logging.info(
        '%s: %s',
        'sucesso na requisição' if 199 < _r.codigo < 299 else 'falha na requisição',
        MapperGetAluno(_r).respostaToAluno()
    )
