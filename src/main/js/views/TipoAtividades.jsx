import React, {Component, PropTypes} from 'react'
import {Crud} from '../crud'
import {TipoAtividadesController} from '../controllers'
import {Input, Row, Col, Grid} from 'react-bootstrap';

class TipoAtividades extends Component {
    componentDidMount = () => TipoAtividadesController.list() // Busca inicial

    searchSchema = (search) =>
        <Input type="text" placeholder="Buscar por nome do Modalidade" autoComplete="off"
               name="nome" degaultValue={search.nome}/>

    listSchema = {
        header: () =>
            <tr>
                <th>ID</th>
                <th>Nome</th>
            </tr>,

        body: (tipoAtividade) =>
            <tr>
                <td>{tipoAtividade.id}</td>
                <td>{tipoAtividade.nome}</td>
            </tr>
    }

    formSchema = (tipoAtividade) =>
        <div>
	        <Grid fluid>
		        <Row className="show-grid">
		          	<Col xs={12}><Input type="text" label="Nome" placeholder="Nome da Modalidade" name="nome" defaultValue={tipoAtividade.nome} autoFocus/></Col>
		        </Row>
		        <Row className="show-grid">
	          	<Col xs={12}><Input type="textarea" label="Observação" placeholder="Observações gerais sobre a atividade" name="observacao" defaultValue={tipoAtividade.observacao} /></Col>
	        </Row>
	      </Grid>
        </div>

    render = () =>
        <Crud title="Modalidade"
              controller={TipoAtividadesController}
              searchSchema={this.searchSchema}
              listSchema={this.listSchema}
              formSchema={this.formSchema} />
}

export default TipoAtividades