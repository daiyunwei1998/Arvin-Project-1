import React from 'react';
import { Select, Tag } from 'antd';

const options = [
  { value: 'S' },
  { value: 'M' },
  { value: 'L' },
];

const tagRender = (props) => {
  const { label, value, closable, onClose } = props;
  const onPreventMouseDown = (event) => {
    event.preventDefault();
    event.stopPropagation();
  };
  return (
    <Tag
      onMouseDown={onPreventMouseDown}
      closable={closable}
      onClose={onClose}
      style={{
        marginInlineEnd: 4,
      }}
    >
      {label}
    </Tag>
  );
};

const ProductAdminSizeSelector = ({ value = [], onChange }) => {
  const handleChange = (value) => {
    console.log(`selected ${value}`);
    onChange(value);
  };

  return (
    <Select
      mode="multiple"
      tagRender={tagRender}
      value={value}
      onChange={handleChange}
      defaultValue={["S", "M", "L"]}
      style={{
        width: '100%',
      }}
      options={options}
    />
  );
};

export default ProductAdminSizeSelector;