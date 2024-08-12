import React from 'react';
import { ColorPicker, Button } from 'antd';
import { Form } from 'antd';

const ProductAdminColorSelect = ({ value = [], onChange }) => {
  const [currentColor, setCurrentColor] = React.useState(null);

  const handleAddColor = () => {
    if (currentColor) {
      const newColors = [...value, currentColor.toHexString()];
      onChange(newColors);
      setCurrentColor(null);
    }
  };

  const handleDeleteColor = (indexToDelete) => {
    const newColors = value.filter((_, index) => index !== indexToDelete);
    onChange(newColors);
  };

  return (
    <div>
      <ColorPicker 
        value={currentColor}
        onChange={setCurrentColor}
      />
      <Button onClick={handleAddColor} style={{ marginLeft: 8 }}>Add Color</Button>
      <div style={{ marginTop: 8 }}>
        {value.map((color, index) => (
          <div key={index} style={{ display: 'inline-block', marginRight: 8 }}>
            <div
              style={{
                width: '30px',
                height: '30px',
                backgroundColor: color,
                borderRadius: '50%',
                display: 'inline-block',
                verticalAlign: 'middle',
              }}
            />
            <Button size="small" onClick={() => handleDeleteColor(index)} style={{ marginLeft: 4 }}>
              Delete
            </Button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductAdminColorSelect;