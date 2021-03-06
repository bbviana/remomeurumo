import React, {Component, PropTypes} from 'react'
import {Request} from '../helpers'
import {id, ids} from '../crud/Associations'
import {Image, Input, ControlLabel, Row, Col, Grid, Panel, Glyphicon, MenuItem, Nav, Navbar, NavBrand, NavItem, Button, Tab, Tabs, ButtonToolbar, ListGroupItem, ListGroup, Accordion} from 'react-bootstrap';

class PlanejamentoAtividades extends Component {
    state = {
        id: "",
        nome: "",
        data: "",
        atividadeGrupos: [],
        tarefas: [],
        equipamentos: [],
        tarefasM: []
    }

    salvarAlteracoes = (event) => {
        event.preventDefault()

        Request.post('api/planejamentoAtividades/salvar', this.state)
        .then(atividade =>{ 
	        this.setState({
	            id: atividade.id,
	            nome: atividade.nome,
	            data: atividade.data,
	            atividadeGrupos: atividade.atividadeGrupos
	        });
        	$.toaster({ title: 'Sucesso', message : 'Registro salvo', settings: {timeout: 3000} });
        }
    )}

    procurarGrupos = () => {
        Request.get('api/planejamentoAtividades/procurarGrupos', {
            id: this.props.id
        })
        .then(atividade => this.setState({
            id: atividade.id,
            nome: atividade.nome,
            data: atividade.data,
            atividadeGrupos: atividade.atividadeGruposTransient
        }))
    }
    
    procurarTarefas = () => {
    	Request.get('api/planejamentoAtividades/procurarTarefas', {
            id: this.props.id
        })
        .then(tarefasRequest => this.setState({
            tarefas: tarefasRequest
        }))
    }
    
    procurarTarefasM = () => {
    	Request.get('api/planejamentoAtividades/procurarTarefasM', {
            id: this.props.id
        })
        .then(tarefasRequest => this.setState({
            tarefasM: tarefasRequest
        }))
    }
    
    procurarEquipamentos = () => {
    	Request.get('api/planejamentoAtividades/procurarEquipamentos', {
            id: this.props.id
        })
        .then(equipamentosRequest => this.setState({
        	equipamentos: equipamentosRequest
        }))
    }
    
    removerAluno = (idAluno, idPlanejamento) => {
    	var atividades = this.state.atividadeGrupos
    	
    	var atividadeEscolhido = atividades.find(element => {
    		return element.id == idPlanejamento
    	})
    	atividadeEscolhido.alunos = atividadeEscolhido.alunos.filter(element => {
    		return element.id != idAluno
    	})
    	this.setState({atividadeGrupos : atividades})
    }
    
    removerColaborador = (idColaborador, idPlanejamento) => {
    	var atividades = this.state.atividadeGrupos
    	
    	var atividadeEscolhido = atividades.find(element => {
    		return element.id == idPlanejamento
    	})
    	atividadeEscolhido.colaboradores = atividadeEscolhido.colaboradores.filter(element => {
    		return element.id != idColaborador
    	})
    	this.setState({atividadeGrupos : atividades})
    }
    
    removerPlanejamento = (idPlanejamento) => {
    	var atividades = this.state.atividadeGrupos
    	
    	var atividadesEscolhido = atividades.filter(element => {
    		return element.id != idPlanejamento
    	})
    	
    	this.setState({atividadeGrupos : atividadesEscolhido})
    }
    
    alterarTarefas = (idPlanejamento, event) => {
    	var atividades = this.state.atividadeGrupos
    	
    	var atividadeEscolhido = atividades.find(element => {
    		return element.id == idPlanejamento
    	})
    	
    	const target = event.target
        let value
        	
        if (target.multiple) {
            value = Array.from(target.options)
                .filter(option => option.selected)
                .map(option => ({id: option.value}))
        } else {
            value = {id: target.value}
        }
    	
        if(!target.value){
            value = null
        } else {
        	atividadeEscolhido.tarefas = value
        	this.setState({atividadeGrupos : atividades})
        }
    	
    }
    
    alterarEquipamentos = (idPlanejamento, event) => {
    	var atividades = this.state.atividadeGrupos
    	
    	var atividadeEscolhido = atividades.find(element => {
    		return element.id == idPlanejamento
    	})
    	
    	const target = event.target
        let value
        	
        if (target.multiple) {
            value = Array.from(target.options)
                .filter(option => option.selected)
                .map(option => ({id: option.value}))
        } else {
            value = {id: target.value}
        }
    	
        if(!target.value){
            value = null
        } else {
        	atividadeEscolhido.equipamentos = value
        	this.setState({atividadeGrupos : atividades})
        }
    	
    }
    
    alterarPlanejamento = (idPlanejamento, event) => {
    	
    	var atividades = this.state.atividadeGrupos
    	
    	var atividadeEscolhido = atividades.find(element => {
    		return element.id == idPlanejamento
    	})
    	
    	atividadeEscolhido.planejamentoDeAula = event.target.value
    	
    	this.setState({atividadeGrupos : atividades})
    }
    
    componentDidMount = () => {
    	this.procurarGrupos()
    	this.procurarTarefas()
    	this.procurarEquipamentos()
    	this.procurarTarefasM()
    }
    
    render = () =>
        <div style={s.app}>
        <Navbar fixedTop fluid inverse>
	        <Navbar.Brand>
	            <a href="?login">Remo meu Rumo</a>
	        </Navbar.Brand>
	        <Nav>
	        	<NavItem eventKey={1} href="#"><Glyphicon glyph="chevron-right"/>&nbsp;&nbsp; Planejamento de Atividades</NavItem>
	        </Nav>
	        <Nav pullRight eventKey={0}> {/* This is the eventKey referenced */}
	            <NavItem eventKey={1} href="#">
	                <Glyphicon glyph="flag"/>&nbsp;&nbsp;Atividade : {this.state.nome},&nbsp;{this.state.data} 
	            </NavItem>
	            
	            <NavItem eventKey={2} onClick={() => window.close()}>
            		<Glyphicon glyph="remove"/>
            	</NavItem>
	        </Nav>
	    </Navbar>
        
            <div className="container-fluid">
                <form style={s.form} onSubmit={this.salvarAlteracoes}>
                    
                    	<div>
                    	                    	
                    	<Tabs id="tabs-grupos">
            		    
            	        {this.state.atividadeGrupos.map((atividadeGrupo, index) => {
            	        	return  <Tab eventKey={index} title={atividadeGrupo.grupo.nome} > <div>
	            	        		<Grid fluid>	
		            	        		<Row className="show-grid">
			            	        		<Col xs={12}>&nbsp;</Col>
			            	          	</Row>
			            	            <Row className="show-grid">
			            	        		<Col xs={6} md={2}><strong>Alunos</strong></Col>
			            	        		<Col xs={6} md={2}><strong>Colaboradores</strong></Col>
			            	        		<Col xs={6} md={2}><strong>Foco no Ensino/Cuidados</strong></Col>
			            	        		<Col xs={6} md={3}><strong>Tarefas</strong></Col>
			            	        		<Col xs={6} md={3}><strong>Equipamentos</strong></Col>
			            	          	</Row>
			            	          	<Row className="show-grid">
			            	          		<Col xs={6} md={2}>
			            	          		 <ListGroup>
				            	          		{atividadeGrupo.alunosTransient.map((aluno, indexAluno) => {
				            	          			return <div ><ListGroupItem key={indexAluno}><Button bsSize="xsmall" active>
				            	          			<Glyphicon style={s.button} onClick={this.removerAluno.bind(this, aluno.id, atividadeGrupo.id)} glyph="minus"/></Button>&nbsp;&nbsp;{aluno.nome}
				            	          			</ListGroupItem>
				            	          			<ListGroupItem bsStyle="danger">{aluno.abc}</ListGroupItem></div>
				            	          		})}
				            	          		</ListGroup>
			            	          		</Col>
			            	          		<Col xs={6} md={2}>
			            	          			<ListGroup>
				            	          		{atividadeGrupo.colaboradores.map((colaborador, indexColaborador) => {
				            	          			return <div><ListGroupItem key={indexColaborador}><Button bsSize="xsmall" active>
				            	          			<Glyphicon style={s.button} onClick={this.removerColaborador.bind(this, colaborador.id, atividadeGrupo.id)} glyph="minus"/></Button>&nbsp;&nbsp;{colaborador.nome}</ListGroupItem></div>
				            	          		})}
				            	          		</ListGroup>	
			            	          		</Col>
			            	          		<Col xs={12} md={2}>
			            	          		<Input type="textarea" label="" onChange={this.alterarPlanejamento.bind(this, atividadeGrupo.id)} name="planejamentoDeAula" defaultValue={atividadeGrupo.planejamentoDeAula} placeholder="Considerações e sugestões pedagógicas" style={{height: 300}} /></Col>
			            	          		<Col xs={12} md={3}>
			            	          			<div>
				            	          			<Input type="select" name="tarefas" defaultValue={ids(atividadeGrupo.tarefas)}  
	                   									 onChange={this.alterarTarefas.bind(this, atividadeGrupo.id)} multiple 
	                   									 style={{height: 300}}>
	                										{this.state.tarefasM[atividadeGrupo.grupo.id].map((element, i) =>
	                    								<option key={i} value={element.id}>{element.nomeCompleto}</option>
	                										)}
	            									</Input>
												</div>
											</Col>
											<Col xs={12} md={2}>
		            	          			<div>
			            	          			<Input type="select" name="equipamentos" defaultValue={ids(atividadeGrupo.equipamentos)} 
			            	          			  onChange={this.alterarEquipamentos.bind(this, atividadeGrupo.id)} multiple  style={{height: 300}}>
                										{this.state.equipamentos.map((element, i) =>
                    								<option key={i} value={element.id}>{element.nome}</option>
                										)}
            									</Input>
											</div>
										</Col>
			            	          	</Row>
		            	        		<Row className="show-grid">
		            	        			<Col xs={12}>&nbsp;</Col>
		            	        		</Row>
			            	          	<Row className="show-grid">
		            	        		<Col xs={12} md={12}>
		            	        			<ButtonToolbar>
		            	        				<Button bsSize="medium" bsStyle="danger" onClick={this.removerPlanejamento.bind(this, atividadeGrupo.id)}>Remover Grupo</Button>
		            	        				<Button bsSize="medium" bsStyle="primary" type="submit">Salvar Alterações</Button>
		            	        			</ButtonToolbar>
			            	            </Col>
		            	          	</Row>
	            	        		<Row className="show-grid">
	            	        			<Col xs={12}>&nbsp;</Col>
	            	        		</Row>
	            	        		
		            	          	</Grid>
            	          	</div>
            	          	</Tab>
            	        })}
            	      
            	      </Tabs>
            	      
                    </div>
                    
                </form>
            </div>
        </div>
}

const s = {
    form: {
        padding: 15,
        margin: "0 auto"
    },
    
    button: {
    	cursor: "pointer"
    },
    app: {
        paddingTop: 50
    }
}

export default PlanejamentoAtividades
