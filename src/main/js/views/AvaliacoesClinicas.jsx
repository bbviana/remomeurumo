import React, {Component, PropTypes} from 'react'
import {Crud} from '../crud'
import {id, ids, handleAssociationChange} from '../crud/Associations'
import {AvaliacoesClinicasController} from '../controllers'
import {Input, Row, Col, Grid} from 'react-bootstrap';

class AvaliacoesClinicas extends Component {
    componentDidMount = () => AvaliacoesClinicasController.list() // Busca inicial

    searchSchema = (search) =>
        <Input type="text" placeholder="Buscar por data da Avaliação" autoComplete="off"
               name="data" degaultValue={search.data}/>

    listSchema = {
        header: () =>
            <tr>
                <th>ID</th>
                <th>Modelo</th>
                <th>Aluno</th>
                <th>Data</th>
            </tr>,

        body: (avaliacaoClinica) =>
            <tr>
                <td>{avaliacaoClinica.id}</td>
                <td>{avaliacaoClinica.modelo.nome}</td>
                <td>{avaliacaoClinica.aluno.nome}</td>
                <td>{avaliacaoClinica.data}</td>
            </tr>
    }

    formSchema = (avaliacaoClinica, {modelos = [], alunos = [], tipos = []}) =>
        <div>
	        <Grid fluid>
		        <Row className="show-grid">
		          	<Col xs={12}  md={6}>
			          	<Input type="select" label="Modelo" name="modelo"
			                defaultValue={id(avaliacaoClinica.modelo)} onChange={handleAssociationChange}>
				            <option value="">Selecione...</option>
				            {modelos.map((element, i) =>
				                <option key={i} value={element.id}>{element.nome}</option>
				            )}
			            </Input>
		            </Col>
		          	<Col xs={12} md={6}><Input type="text" label="Data" placeholder="Data da Avaliacao" name="data" defaultValue={avaliacaoClinica.data}/></Col>
		        </Row>
		        <Row className="show-grid">
		          	<Col xs={12}>
			          	<Input type="select" label="Aluno" name="aluno"
			                defaultValue={id(avaliacaoClinica.aluno)} onChange={handleAssociationChange}>
				            <option value="">Selecione...</option>
				            {alunos.map((element, i) =>
				                <option key={i} value={element.id}>{element.nome}</option>
				            )}
			            </Input>
		            </Col>
		        </Row>
		        <Row className="show-grid">
	          		<Col xs={12}><Input type="textarea" label="Comentário" placeholder="Comentários da Avaliação" name="comentario" defaultValue={avaliacaoClinica.comentario} /></Col>
	          	</Row>
	          	<Row className="show-grid">
	          		<Col xs={6} md={4}>nome</Col>
	          		<Col xs={6} md={4}>valor</Col>
	          		<Col xs={12} md={4}>unidade de medida</Col>
        		</Row>
	          	{tipos.map((iterador, i) =>
	          	 <Row className="show-grid">
	        		<Col xs={6} md={4}>{iterador.nome}</Col>
	        		<Col xs={6} md={4}><Input type="text" label="" placeholder="Valor" name="data"/></Col>
	        		<Col xs={12} md={4}>{iterador.sigla}</Col>
	          	</Row>
	          	
	          	 )}
	      </Grid>
        </div>

    render = () =>
        <Crud title="Avaliação Clinica"
              controller={AvaliacoesClinicasController}
              searchSchema={this.searchSchema}
              listSchema={this.listSchema}
              formSchema={this.formSchema} />
}

export default AvaliacoesClinicas