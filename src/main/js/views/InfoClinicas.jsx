import React, {Component, PropTypes} from 'react'
import {Crud} from '../crud'
import {id, ids, handleAssociationChange} from '../crud/Associations'
import {InfoClinicasController} from '../controllers'
import {Input, FormControl, FormGroup, ControlLabel, Row, Col, Grid} from 'react-bootstrap';

class InfoClinicas extends Component {
    componentDidMount = () => InfoClinicasController.list() // Busca inicial

    searchSchema = (search) =>
        <FormControl type="text" placeholder="Buscar por valor da Informação" autoComplete="off"
               name="valor" degaultValue={search.valor}/>

    listSchema = {
        header: () =>
            <tr>
                <th>ID</th>
                <th>Valor</th>
                <th>Tipo</th>
            </tr>,

        body: (infoClinica) =>
            <tr>
                <td>{infoClinica.id}</td>
                <td>{infoClinica.valor}</td>
                <td>{infoClinica.tipo}</td>
            </tr>
    }

    formSchema = (infoClinica, {tipos = []}) =>
        <div>
        <FormGroup controlId="formControlsFile">
	        <Grid fluid>
		        <Row className="show-grid">
		          	<Col xs={12}><ControlLabel>Valor</ControlLabel><FormControl type="text" placeholder="Valor" name="valor" defaultValue={infoClinica.nome} autoFocus/></Col>
		        </Row>
	          	 <Row className="show-grid">	
			        	<Col xs={12}>
			        	<ControlLabel>Tipo</ControlLabel>
			        	<FormControl componentClass="select" label="Tipo" name="tipo"
					                defaultValue={id(infoClinica.tipo)} onChange={handleAssociationChange}>
					            <option value="">Selecione...</option>
					            {tipos.map((element, i) =>
					                <option key={i} value={element.id}>{element.nome}</option>
					            )}
				            </FormControl>
			            </Col>  	
		        </Row>
	      </Grid>
	      </FormGroup>
        </div>

    render = () =>
        <Crud title="Informação"
              controller={InfoClinicasController}
              searchSchema={this.searchSchema}
              listSchema={this.listSchema}
              formSchema={this.formSchema} />
}

export default InfoClinicas